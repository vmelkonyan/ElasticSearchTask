<#import "parts/common.ftl" as c>
<@c.page>

    <div><label>User</label>
        <label> Count ${user_cont}</label></div>


    <table class="table table-bordered table-dark">
        <thead>
        <tr>
            <th scope="col">Age range</th>
            <th scope="col">0 - 20</th>
            <th scope="col">20 - 50</th>
            <th scope="col">50 - 99</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row">1</th>
            <td>${userFrom0to20}</td>
            <td>${userFrom20to50}</td>
            <td>${userFrom50to99}</td>
        </tr>
        </tbody>
    </table>


    <div><span>Products</span>
        <label> Count ${product_cont}</div>

    <table class="table table-bordered table-dark">
        <thead>
        <tr>
            <th scope="col">Price range</th>
            <th scope="col">0 - 20</th>
            <th scope="col">20 - 50</th>
            <th scope="col">50 - 99</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row">1</th>
            <td>${prodFrom0to20}</td>
            <td>${prodFrom20to50}</td>
            <td>${prodFrom50to99}</td>
        </tr>
        </tbody>
    </table>

    <form method="get" action="/deleteAll" class="form-inline my-2 my-lg-0">
        <button class="btn btn-outline-primary bg-light my-2 my-sm-0" type="submit">Delete All</button>
    </form>
    <form method="post" action="/find" class="form-inline my-2 my-lg-0">
        <div class="form-group mt-2 my-2">
            <select class="custom-select col-sm-20 my-2" name="selectType">
                <option value="User" name="user">User</option>
                <option value="Product" name="product">Product</option>
            </select>
        </div>
        <div class="group mt-2 my-2">
            <input type="text" class="form-control col-sm-3 ml-0 ml-2" id="selectFrom" name="selectFrom"
                   placeholder="Select From" maxlength="2" required>
            <input type="text" class="form-control col-sm-3 ml-2 my-4" id="selectTo" name="selectTo"
                   placeholder="Select To" maxlength="2" required>
            <button class="btn btn-outline-primary bg-light my-4 ml-2" type="submit">Search</button>
        </div>
    </form>


    <div><span>Search result</span>
        <label>${find_res}</div>
</@c.page>