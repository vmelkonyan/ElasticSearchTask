package com.lilas.elasticsearchtask.services;

import com.alibaba.fastjson.JSON;
import com.lilas.elasticsearchtask.constants.KeyConstants;
import com.lilas.elasticsearchtask.models.Product;
import com.lilas.elasticsearchtask.models.Products;
import com.lilas.elasticsearchtask.models.User;
import com.lilas.elasticsearchtask.models.Users;
import com.lilas.elasticsearchtask.repository.ProductRepo;
import com.lilas.elasticsearchtask.repository.UserRepo;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class MainService {
    @Value("${user.count.and.percent}")
    String userCountAndPercent;
    @Value("${product.count.and.percent}")
    String productCountAndPercent;

    private final ElasticsearchOperations es;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    public MainService(ElasticsearchOperations es, ProductRepo productRepo, UserRepo userRepo) {
        this.es = es;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public List<Product> findAllProd() {
        List<Product> productArrayList = new ArrayList<>();
        Iterable<Product> products = productRepo.findAll();
        products.forEach(productArrayList::add);
        return productArrayList;
    }

    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        Iterable<User> users = userRepo.findAll();
        users.forEach(userList::add);
        return userList;
    }

    public Integer userCount() {
        List<User> userList = new ArrayList<>();
        Iterable<User> users = userRepo.findAll();
        users.forEach(userList::add);
        return userList.size();
    }

    public int profCount() {
        List<Product> products = new ArrayList<>();
        Iterable<Product> productList = productRepo.findAll();
        productList.forEach(products::add);
        return products.size();
    }

    public void unzip(final InputStream inputStream, final String unzipLocation) throws IOException {
            if (!(Files.exists(Paths.get(unzipLocation)))) {
                Files.createDirectories(Paths.get(unzipLocation));
            }
            try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry entry = zipInputStream.getNextEntry();
                while (entry != null) {
                    Path filePath = Paths.get(unzipLocation, entry.getName());
                    if (!entry.isDirectory()) {
                        unzipFiles(zipInputStream, filePath);
                    } else {
                        Files.createDirectories(filePath);
                    }

                    zipInputStream.closeEntry();
                    if (!entry.isDirectory()) {
                        convertFileToObject(new File(String.valueOf(filePath)));
                    }
                    entry = zipInputStream.getNextEntry();
                }
            }
    }

    public void unzipFiles(final ZipInputStream zipInputStream, final Path unzipFilePath) throws IOException {

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(unzipFilePath.toAbsolutePath().toString()))) {
            byte[] bytesIn = new byte[1024];
            int read = 0;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }

    }

    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    private boolean convertFileToObject(File file) {
        JAXBContext jaxbContext = null;
        Unmarshaller jaxbUnmarshaller = null;
        try {
            if (file.getName().contains("products")) {
                jaxbContext = JAXBContext.newInstance(Products.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Products prodObj = (Products) jaxbUnmarshaller.unmarshal(file);
                Iterable<Product> iterable = Arrays.asList(prodObj.getProduct());
                productRepo.save(iterable);

            } else if (file.getName().contains("users")) {
                jaxbContext = JAXBContext.newInstance(Users.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Users user = (Users) jaxbUnmarshaller.unmarshal(file);
                Iterable<User> userIiterable = Arrays.asList(user.getUser());
                userRepo.save(userIiterable);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void addToElastic(InputStream inputStream ,Model model) throws IOException {
        Path source = Paths.get(this.getClass().getResource("/").getPath());
        Path outFolder = Paths.get(source.toAbsolutePath() + "/outFolder/");
        unzip(inputStream, outFolder.toString());

        initUi(model, null);

    }

    public void find(Model model, Map<String, String> values) {
        initUi(model, values);
    }

    public void deleteAll(Model model) {
        userRepo.deleteAll();
        productRepo.deleteAll();

        initUi(model, null);
    }

    private void initUi(Model model, Map<String, String> values) {
        long totalUserCount = userCount();
        long totalProdCount = profCount();
        model.addAttribute("user_cont", totalUserCount);
        model.addAttribute("userFrom0to20", userStatisticAndPercent(0l, 20l, totalUserCount));
        model.addAttribute("userFrom20to50", userStatisticAndPercent(20l, 50l, totalUserCount));
        model.addAttribute("userFrom50to99", userStatisticAndPercent(50l, 99l, totalUserCount));
        model.addAttribute("product_cont", totalProdCount);
        model.addAttribute("prodFrom0to20", productStatisticAndPercent(0l, 20l, totalProdCount));
        model.addAttribute("prodFrom20to50", productStatisticAndPercent(20l, 50l, totalProdCount));
        model.addAttribute("prodFrom50to99", productStatisticAndPercent(50l, 99l, totalProdCount));
        if (values != null) {
            String selectedType = null;
            Long from, to;
            if (values.get("selectType") != null) {
                selectedType = (String) values.get("selectType");
            }
            from = Long.valueOf(values.get("selectFrom"));
            to = Long.valueOf(values.get("selectTo"));
            assert selectedType != null;
            if (selectedType.toLowerCase().contains("user")) {
                model.addAttribute("find_res", userStatisticAndPercent(Math.min(from, to), Math.max(from, to), totalUserCount));
            } else {
                model.addAttribute("find_res", productStatisticAndPercent(from, to, totalProdCount));
            }

        } else {
            model.addAttribute("find_res", "");
        }
    }

    public String productStatisticAndPercent(Long from, Long to, long totalCount) {
        SearchResponse response = es.getClient().prepareSearch(KeyConstants.DOCUMENT_INDEX_NAME)
                .setTypes("product")
                .setPostFilter(QueryBuilders.rangeQuery("price").from(from).to(to))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .execute()
                .actionGet();


        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<Product> results = new ArrayList<>();
        searchHits.forEach(
                hit -> results.add(JSON.parseObject(hit.getSourceAsString(), Product.class)));
        double percent = calculatePercentage(response.getHits().getTotalHits(), totalCount);
        return String.format(productCountAndPercent, response.getHits().getTotalHits(), percent);
    }

    public String userStatisticAndPercent(Long from, Long to, long totalCount) {
        String fromReq = "now-" + to + "y/d";
        String toReq = "now-" + from + "y/d";
        SearchResponse response = es.getClient().prepareSearch(KeyConstants.DOCUMENT_INDEX_NAME)
                .setTypes("user")
                .setPostFilter(QueryBuilders.rangeQuery("birthday").from(fromReq).to(toReq))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .execute()
                .actionGet();


        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<User> results = new ArrayList<User>();
        searchHits.forEach(
                hit -> results.add(JSON.parseObject(hit.getSourceAsString(), User.class)));
        double percent = calculatePercentage(response.getHits().getTotalHits(), totalCount);
        return String.format(userCountAndPercent, response.getHits().getTotalHits(), percent);
    }
}
