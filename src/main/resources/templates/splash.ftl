<#import "parts/common.ftl" as c>
<@c.page>
    <div>Elastic Search</div>

    <form method="post" action="/upload" enctype="multipart/form-data" class="my-2 my-lg-0">
            <div class="form-group">
                <label for="exampleFormControlFile1">Choose zip file</label>
                <input type="file" class="form-control-file bg-light" name="file">
            </div>
        <button class="btn btn-outline-primary bg-light my-2 my-sm-0" type="submit">Extract File</button>
    </form>
    ${error_message?ifExists}
</@c.page>