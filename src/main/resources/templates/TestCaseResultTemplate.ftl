<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.css" rel="stylesheet" />
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet" />
    <title>${businessName}</title>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <h3>测试用例:&nbsp;${businessName}</h3>
            <table class="table table-striped table-bordered table-hover">
                <tr>
                    <th style="text-align:center">步骤名称</th>
                    <th style="text-align:center">步骤URL</th>
                    <th style="text-align:center">响应状态</th>
                    <th style="text-align:center">响应时间</th>
                    <th style="text-align:center">验证字段</th>
                    <th style="text-align:center">实际结果</th>
                    <th style="text-align:center">期望结果</th>
                    <th style="text-align:center">是否通过</th>
                </tr>
                <#list stepFMResultList as step>
                    <tr>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.stepName}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.stepUrl}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.httpStatusCode}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.httpResponseTime}</td>
                        <td>${step.resultKeyList[0]}</td>
                        <td>${step.actualValueList[0]}</td>
                        <td>${step.expectValueList[0]}</td>
                        <td>${step.resultValueList[0]}</td>
                    </tr>
                    <#if step.verifyLength gt 1>
                        <#list 1..step.verifyLength as verify>
                            <#if verify_index != 0>
                                <tr>
                                    <td>${step.resultKeyList[verify_index]}</td>
                                    <td>${step.actualValueList[verify_index]}</td>
                                    <td>${step.expectValueList[verify_index]}</td>
                                    <td>${step.resultValueList[verify_index]}</td>
                                </tr>
                            </#if>
                        </#list>
                    </#if>
                </#list>
            </table>
        </div>
        <div class="col-md-1"></div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>