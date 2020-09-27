<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<%--//绝对路径--%>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

		<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
		<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
		<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            //打开模态窗口

			// $("#createActivityModal").modal("show");
            $.ajax({
                url:"workbench/activity/getUserList.do",
                type:"get",
                dataType:"json",
                success:function (data) {
                 //分析data的返回数据
                    /***
					 * 返回的肯定是一个list集合，里面保存着一个个的用户
					 * data{
					 * [{user1},{user2},{user3}]
					 * }
					 *
                     */
                    var html = "<option></option>";
                    //遍历出来的每一个n，就是每一个user对象
                    $.each(data,function (i,n) {
                        html += "<option value='"+n.id+"'>"+n.name+"</option>";
                    });
					$("#create-owner").html(html);

					//在jsp中使用EL表达式，需要将其用双引号引起来。
					var id = "${user.id}";
                    //将当前登陆的用户设置为默认选项
					$("#create-owner").val(id);
                    $("#createActivityModal").modal("show");
                }
            })
        })
		$("#saveBtn").click(function () {
		    //alert(123)

            $.ajax({
                url:"workbench/activity/save.do",
                data:{
                    "owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
                },
                type:"post",
                dataType:"json",
                success:function (data) {
					if(data.success){
						//添加成功后，刷新市场活动列表
                        //pageList(1,5);
						pageList($("#activityPage").bs_pagination('getOption','currentPage')
							,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
						//清空历史数据
						$("#activityAddForm")[0].reset();

						//alert("添加成功");
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");

					}else {
						alert("添加市场活动失败！！！")
					}

                }
            })
        });
		//页面加载完毕后 出发该方法，获取数据列表
		pageList(1,5);
		//为查询按钮绑定事件
		$("#searchBtn").click(function () {
		    //应该将搜索框中得内容保存在隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
            $("#hidden-owner").val($.trim($("#search-owner").val()));
            $("#hidden-startDate").val($.trim($("#search-startDate").val()));
            $("#hidden-endDate").val($.trim($("#search-endDate").val()));
            pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
        });
        //选中全选实现全选效果
        $("#qx").click(function () {
            //alert(123)
			$("input[name=xz]").prop("checked",this.checked);

        })
        // $("input[name=xz]").click(function () {
			// alert(123)
        // })
		//因为动态生成的元素不能绑定普通的事件，需要用on的方式
		/*
		公式是：$(需要绑定元素的有效外层元素--不是动态生成的元素).on（绑定的事件，绑定的Jquery对象，回调函数）
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
        });
		//删除活动数据操作
		$("#deleteBtn").click(function () {
		    //拿到的选中的数据
			var $xz = $("input[name=xz]:checked");
			if ($xz.length==0){
			    alert("请选中您要删除的数据")
			} else {
			    if(confirm("确定删除吗？")){
                    var param = "";
                    for (i=0;i<$xz.length;i++){
                        param +="id="+$($xz[i]).val();
                        //如果不是最后一个元素需要追加一个&符号
                        if(i<$xz.length-1){
                            param+="&";
                        }
                    }
                    alert(param)
                    $.ajax({
                        url:"workbench/activity/delete.do",
                        data:param,
                        type:"post",
                        dataType:"json",
                        success:function (data) {
                            if (data.success){
                                pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
                            } else{
                                alert("删除失败！！")
                            }
                        }
                    })
				}

			}
        })
		//为修改按钮绑定事件，打开修改操作的模态窗口
		//打开模态窗口前 需要根据用户所选择的数据从后台获取这条数据，并展现给用户
		$("#editBtn").click(function (){
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
			    alert("请选择你要修改的记录")
			}else if($xz.length>1){
			    alert("只能选择一条记录进行修改")
				//到这里肯定是只选择了一条
			}else {
			    var id = $xz.val();
                $.ajax({
                    url:"workbench/activity/getUserListAndActivity.do",
                    data: {"id":id},
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        /*
                        data:
                        {"uList":[{user1},{user2}],"activity":{a}}
                         */
						var html = "<option></option>";
						$.each(data.uList,function (i,n) {
							html+="<option value='"+n.id+"'>"+n.name+"</option>"
                        });
						$("#edit-owner").html(html);
						//处理单条activity
						$("#edit-id").val(data.a.id);
                        $("#edit-name").val(data.a.name);
                        $("#edit-owner").val(data.a.owner);
                        $("#edit-startDate").val(data.a.startDate);
                        $("#edit-endDate").val(data.a.endDate);
                        $("#edit-cost").val(data.a.cost);
                        $("#edit-description").val(data.a.description);

                        //打开模态窗口
						$("#editActivityModal").modal("show");

                    }
                })
			}

        })
		//点击更新按钮，更新后的数据保存到数据库中
		$("#updateBtn").click(function () {
			//alert(123)
            $.ajax({
                url:"workbench/activity/update.do",
                data:{
					"id":$("#edit-id").val(),
					"owner":$("#edit-owner").val(),
                    "name":$("#edit-name").val(),
                    "startDate":$("#edit-startDate").val(),
                    "endDate":$("#edit-endDate").val(),
                    "cost":$("#edit-cost").val(),
                    "description":$("#edit-description").val()
                },
                type:"post",
                dataType:"json",
                success:function (data) {
                    if(data.success){
                        pageList($("#activityPage").bs_pagination('getOption','currentPage')
                            ,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
                        $("#editActivityModal").modal("hide")
                        //alert("更新成功！！")
					}else {
                        alert("更新失败")
					}
                }
            })
        })
	});
    /***
	 *
     * @param pageNo  页码
     * @param pageSize 每页展现的数据条数
	 * 什么情况下需要刷新市场活动列表？？ 需要调用pageList方法
	 * 1、点击“市场活动”按钮
	 * 2、创建/修改/删除  完毕之后，需要刷新活动列表
	 * 3、翻页后也需要刷新活动列表
	 * 4、点击查询按钮时，需要刷新活动列表
     */
    function pageList(pageNo,pageSize) {
		//alert("展现市场活动的信息列表！！！！
		$("#qx").prop("checked",false);
		//查询前，需要将隐藏域中得内容，赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startTime").val($.trim($("#hidden-startDate").val()));
		$("#search-endTime").val($.trim($("#hidden-endDate").val()));
        $.ajax({
            url:"workbench/activity/pageList.do",
            data:{
                "pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startTime":$.trim($("#search-startTime").val()),
				"endTime":$.trim($("#search-endTime").val())
            },
            type:"get",
            dataType:"json",
            success:function (data) {
                //分析data的返回数据
                /**
				 * data
				 * [{市场活动列表},{1},{2}]
				 * 查询出来的总记录数
				 * {"total":100}
                 */
                var html = "";

                //每一个n就是每一个市场活动对象
                $.each(data.dataList,function (i,n) {

                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';

                })

                $("#activityBody").html(html);
                //计算总页数
                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                //数据处理完毕后，对前端展现分页信息
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
            }
        })
    }

</script>
</head>
<body>

	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="activityAddForm" role="form">
					
						<div class="form-group">
							<label for="create-marketOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" id="edit-id">
					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" time id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" time id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表123</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称123</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">--%>
							<%--<td><input type="checkbox" /></td>--%>
							<%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
							<%--<td>2020-10-10</td>--%>
							<%--<td>2020-10-20</td>--%>
						<%--</tr>--%>
                        <%--<tr class="active">--%>
                            <%--<td><input type="checkbox" /></td>--%>
                            <%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
                            <%--<td>2020-10-10</td>--%>
                            <%--<td>2020-10-20</td>--%>
                        <%--</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>