<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.css" rel="stylesheet" />
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet" />
    <title>${businessName}</title>
    <style>
        .max{width:95%;height:auto;}
        .min{width:240px;height:auto;}
    </style>
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
                    <th style="text-align:center">详细查看</th>
                </tr>
                <#list stepFMResultList as step>
                    <tr>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.stepName}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;">${step.stepUrl}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;text-align:center;">${step.httpStatusCode}</td>
                        <td rowspan="${step.verifyLength}" style="vertical-align:middle;text-align:center;">${step.httpResponseTime}毫秒</td>
                        <#--如果没有验证点-->
                        <#if step.verifyFieldList?? && (step.verifyFieldList ? size > 0)>
                            <td style="vertical-align:middle;">${step.verifyFieldList[0]}</td>
                        <#else>
                            <td></td>
                        </#if>
                        <#if step.actualValueList?? && (step.actualValueList ? size > 0)>
                            <td style="vertical-align:middle;">${step.actualValueList[0]}</td>
                        <#else>
                            <td></td>
                        </#if>
                        <#if step.expectValueList?? && (step.expectValueList ? size > 0)>
                            <td style="vertical-align:middle;">${step.expectValueList[0]}</td>
                        <#else>
                            <td></td>
                        </#if>
                        <#if step.resultValueList?? && (step.resultValueList ? size > 0)>
                            <#if step.resultValueList[0] == '验证通过'>
                                <td style="vertical-align:middle;text-align:center;font-weight:bold;color:#336633">${step.resultValueList[0]}</td>
                            <#elseif step.resultValueList[0] == '验证失败'>
                                <td style="vertical-align:middle;text-align:center;font-weight:bold;color:#662139">${step.resultValueList[0]}</td>
                            <#else>
                                <td style="vertical-align:middle;text-align:center;font-weight:bold">${step.resultValueList[0]}</td>
                            </#if>
                        <#else>
                            <td></td>
                        </#if>
                        <td rowspan="${step.verifyLength}" style="text-align:center;vertical-align:middle">
                            <#if step.sqlFetchEntityList?? && (step.sqlFetchEntityList ? size > 0)>
                                <button type="button" class="btn btn-primary openDetailBtn">查看</button>
                            <#else>
                                <button type="button" class="btn btn-primary" disabled="disabled">查看</button>
                            </#if>
                        </td>
                    </tr>
                    <#--如果验证点大于1个-->
                    <#if step.verifyLength gt 1>
                        <#list 1..step.verifyLength as verify>
                            <#if verify_index != 0>
                                <tr>
                                    <td style="vertical-align:middle;">${step.verifyFieldList[verify_index]}</td>
                                    <td style="vertical-align:middle;">${step.actualValueList[verify_index]}</td>
                                    <td style="vertical-align:middle;">${step.expectValueList[verify_index]}</td>
                                    <#if step.resultValueList[verify_index] == '验证通过'>
                                        <td style="vertical-align:middle;text-align:center;font-weight:bold;color:#336633">${step.resultValueList[verify_index]}</td>
                                    <#elseif step.resultValueList[verify_index] == '验证失败'>
                                        <td style="vertical-align:middle;text-align:center;font-weight:bold;color:#662139">${step.resultValueList[verify_index]}</td>
                                    <#else>
                                        <td style="vertical-align:middle;text-align:center">${step.resultValueList[verify_index]}</td>
                                    </#if>
                                </tr>
                            </#if>
                        </#list>
                    </#if>
                    <#--<#if (step.imageName)??>-->
                        <#--<tr class="detailInfo" style="display:none;">-->
                            <#--<td colspan="9" style="text-align:center;vertical-align:middle">-->
                                <#--<img src="${step.imageName}" style="max-width: 100%;padding-bottom: 5px;padding-top: 5px;" class='imgShow min'/>-->
                            <#--</td>-->
                        <#--</tr>-->
                    <#--</#if>-->
                    <#--如果SQL结果不为空-->
                    <#if step.sqlFetchEntityList?? && (step.sqlFetchEntityList ? size > 0)>
                        <tr class="detailInfo" style="display:none;">
                            <td colspan="9">
                                <table class="table table-striped table-bordered table-hover">
                                    <#list step.sqlFetchEntityList as fetchEntity>
                                        <#if fetchEntity.singleFetchResultList??>
                                            <tr>
                                                <th colspan="9" style="vertical-align:middle;text-align:center;color:#336633">${fetchEntity.sql}</th>
                                            </tr>
                                            <tr>
                                                <td colspan="9" style="vertical-align:middle;text-align:center">
                                                    <table class="table table-striped table-bordered table-hover" style="table-layout: fixed">
                                                        <tr>
                                                            <#list fetchEntity.columnList as column>
                                                                <th style="text-align: center">${column}</th>
                                                            </#list>
                                                        </tr>
                                                        <#list fetchEntity.singleFetchResultList as itemResultLine>
                                                            <tr>
                                                                <#list itemResultLine as itemValue>
                                                                    <td style="text-align:center">${itemValue}</td>
                                                                </#list>
                                                            </tr>
                                                        </#list>
                                                    </table>
                                                </td>
                                            </tr>
                                        </#if>
                                    </#list>
                                </table>
                            </td>
                        </tr>
                    </#if>
                </#list>
            </table>
        </div>
        <div class="col-md-1"></div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function(){
        $(document).on('click','.openDetailBtn',function(){
            var $detailInfo=$(this).parents('tr').nextAll('.detailInfo').eq(0);
            if($detailInfo.css('display')==='none'){
                $detailInfo.show();
            }else{
                $detailInfo.hide();
            }
        });
        // $(document).on('click', '.imgShow', function() {
        //     $(this).toggleClass('min');
        //     $(this).toggleClass('max');
        // })
    });
</script>
</body>
</html>