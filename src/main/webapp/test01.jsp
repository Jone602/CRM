<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/9/2
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        $.ajax({
            url:"",
            data:{

            },
            type:"get",
            dataType:"json",
            success:function (data) {

            }
        });
        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "top-left"
        });



        var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

        $("#activityPage").bs_pagination({
            currentPage: pageNo, // 页码
            rowsPerPage: pageSize, // 每页显示的记录条数
            maxRowsPerPage: 20, // 每页最多显示的记录条数
            totalPages: totalPages, // 总页数
            totalRows: data.total, // 总记录条数

            visiblePageLinks: 3, // 显示几个卡片

            showGoToPage: true,
            showRowsPerPage: true,
            showRowsInfo: true,
            showRowsDefaultInfo: true,

            //该回调函数时在，点击分页组件的时候触发的
            onChangePage : function(event, data){
                pageList(data.currentPage , data.rowsPerPage);
            }
        });
        //自动补全
        $("#create-customerName").typeahead({
            source: function (query, process) {
                $.post(
                    "workbench/transaction/getCustomerName.do",
                    { "name" : query },
                    function (data) {
                        //alert(data);
                        process(data);
                    },
                    "json"
                );
            },
            delay: 1500
        });
    </script>
</head>
<body>

</body>
</html>
