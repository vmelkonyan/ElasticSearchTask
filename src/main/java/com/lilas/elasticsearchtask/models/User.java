package com.lilas.elasticsearchtask.models;

import com.lilas.elasticsearchtask.constants.KeyConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Document(indexName = KeyConstants.DOCUMENT_INDEX_NAME, type = "user")
@Getter
@Setter
public class User {
    private String birthday;

    private String country;

    private String gender;

    private String mail;

    private String last_name;

    private String id;

    private String ip_address;

    private String first_name;

    private String email;
}
