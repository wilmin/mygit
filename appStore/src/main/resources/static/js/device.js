var user;  
$(document).ready(function () {
    //第一次进入页面就自动进行一次在线统计
    // if (sessionStorage.getItem("statistic") == null) {
    //     getBox();
    // }
    user = new User();  
    user.init(); 
    queryBoxList();
    
    //刷新按钮
    $('#refresh_data').bind("click", detailAdPageLoad);
   /* $('#get_network').bind("click", query_network);*/
    $('#get_app_list').bind("click", app_selectWithFilterClick);
    $('#find_ping').bind("click", find_selectWithFilterClick);
    //刷新奔溃日志
    $('#refresh_log').bind("click", log_selectWithFilterClick);
    
    $('#deleteDevice').bind("click", deleteDeviceClick);
    //黑白名单多条删除按钮
    $('#deleteBWList').bind("click", deleteBWListClick);
    
    $('#restartDevice').bind("click", restartDeviceClick);
    $('#factoryReset').bind("click", factoryResetDeviceClick);
    /*查看详情*/
    $('#detailAd').bind("click", detailAdResetDeviceClick);
    /*Excel模板按钮*/
    $('#excelTemp').bind("click", excelTempClick);
    
    
    $('#confirm_selectDeviceWithFilter').bind("click", confirm_selectDeviceWithFilterClick);
    $('#submitEditDevice').bind("click", submitEditDeviceClick);
    /*点击选项卡菜单*/
    $('#libase').bind("click", baseDeviceClick);
    $('#linetwork').bind("click", networkDeviceClick);
    $('#liapp').bind("click", queryApp);
    $('#liping').bind("click", queryPing);
    $('#ligetlog').bind("click", queryLogs);
    
    $('#refresh_data').bind("click", refreshData);
    $('#get_app_list').bind("click", getapplist);
    
    var beginIndex = location.pathname.indexOf("/", 1);
	var endIndex = location.pathname.indexOf(".", 0);
	var pageName = location.pathname.substring(beginIndex + 1, endIndex);
	
	switch (pageName) {
		case "black-white":
			detailAdPageLoad();
			break;
		case "automatic":
			//静默配置列表显示
			query_network();
			//选择机型版本下拉框
			queryAutoModelList();
			//选择APP升级表下拉框
			queryAutoAPPNameList();
			break;
		case "silent":
			//静默配置列表显示
			queryApp();
			//选择机型版本下拉框
			queryStaticModelList();
			//选择APP升级表下拉框
			queryStaticAPPNameList();
			break;
		case "ota-filter":
			queryPing();
			break;
		case "app-store":
			//显示APP商城配置列表
			query_AppStore_table();
			//选择机型版本下拉框
			queryAppStoreModelList();
			//选择APP升级表下拉框
			queryAppStoreAPPNameList();
			break;
		case "app-upgrade":
			query_APP_Upgrade();
		break;
		case "device-addsuccess":
		break;
	}
});

/********
 * begin白名单过滤列表
 * ******
 */
//白名单编辑和删除按钮
window.whiteEvents = {
        'click .deleteWhite': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-WhiteList",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#ref_data_table').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editWhite': function (e, value, row, index) {
        	$('#editWhiteModel').modal('show');
        	$('#editWhite_id').val(row.id);
        	$('#editWhite_Model').val(row.model);
        	$('#editWhite_SN').val(row.sn);
        	$('#editWhite_Mac').val(row.mac);
        	//下拉框显示选中值
        	set_select_checked('editWhite_status', row.filterState);
        	
        }
}

//下拉框选中方法
function set_select_checked(selectId, checkValue){  
    var select = document.getElementById(selectId);  
    for (var i = 0; i < select.options.length; i++){  
        if (select.options[i].value == checkValue){ 
            select.options[i].selected = true;  
            break;  
        }  
    }  
}

//黑白名单多条删除按钮
function deleteBWListClick() {
    var selected = JSON.stringify($('#ref_data_table').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('Are you sure to delete this information?', {
    		  time: 0 //不自动关闭
    		  ,btn: ['Confirm', 'Cancel']
    		  ,yes: function(index){
    		    layer.close(index);
    		    for (var i = 0; i < selected.length; i++) {
    	            $.ajax({
    	                type: "post",
    	                url: "delete-WhiteList",
    	                contentType: 'application/json',
    	                data: JSON.stringify({id: selected[i].id}),
    	                success: function (result) {
    	                	layer.msg(result.msg);
                			$('#ref_data_table').bootstrapTable('refresh');
    	                },
    	                error: function (result, xhr) {
    	                    console.log(result);
    	                    console.log(xhr);
    	                }
    	            })
    	        }
    	}});

    	
    } else {
    	layer.msg('You select at least one record.',{icon: 5});
    }
    

}

//添加白名单信息
function submit_addWhiteClick() {
	if ($("#addWhite_Model").val()) {
		var data = {
			"model":$('#addWhite_Model').val(),
			"sn": $('#addWhite_SN').val(),
			"mac": $("#addWhite_Mac").val(),
			"filterState": $("#addWhite_status").val()
		}
		//首先检查黑白名单是否重复
		$.ajax({
			type: "post",
			url: "check-WhiteList",
			contentType : 'application/json',
			dataType:"json",
			data: JSON.stringify(data),
			success: function(result) {
				var status = $("#addWhite_status").val();
				//判断是否已存在名单信息中
				if (result.code == 1) {
					layer.msg(result.msg);
					$("#addWhite_Model").focus();
				} 
				else if(result.code==0){
					$.ajax({
						type: "post",
						url: "add-WhiteList",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							$('#addWhiteModel').modal('hide');
							layer.msg(result.msg);
							$('#ref_data_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.msg(result.msg,{icon:2});
						}
					});
				}else{
					layer.msg(result.msg);
					$("#addWhite_Model").focus();
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
	
	} else {
		layer.msg('Please fill your model name!', {icon: 5});
	}
}

//点击白名单编辑提交按钮
$("#editWhite_Submit").click(function() {
 	if ($("#editWhite_Model").val()) {
 		var data = {
 				"id":$('#editWhite_id').val(),
 				"model":$('#editWhite_Model').val(),
 				"sn": $('#editWhite_SN').val(),
 				"mac": $("#editWhite_Mac").val(),
 				"filterState": $("#editWhite_status").val()
 			}
 		
 		//首先检查黑白名单是否重复
		$.ajax({
			type: "post",
			url: "check-WhiteList",
			contentType : 'application/json',
			dataType:"json",
			data: JSON.stringify(data),
			success: function(result) {
				var status = $("#addWhite_status").val();
				//判断是否已存在名单信息中
				if (result.code == 1) {
					layer.msg(result.msg);
					$("#editWhite_Model").focus();
				} 
				else if(result.code==0){
					$.ajax({
						type: "post",
						url: "update-WhiteList",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							$('#editWhiteModel').modal('hide');
							layer.msg(result.msg);
							$('#ref_data_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.msg(result.msg,{icon:2});
						}
					});
				}else{
					layer.msg(result.msg);
					$("#editWhite_Model").focus();
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
 		
	} else {
			layer.msg('Please fill your model name!',{icon:2});
	}
});

//黑白名单状态显示
function whiteStatusFun(value,row,index) {
	//open是1 close是0
	if(value=="-1") {
		return '<font color="red">black</font>';
	}
	if(value=="1") {
		return '<font color="green">white</font>';
	}
	return '-';
}

//动态加载白名单按钮
function whiteOperationFun(value,row,index) {  
	return ['<button type="button" permission="white:edit" class="btn btn-info btn-sm editWhite" data-toggle="modal" data-target="editProductModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="white:delete" class="btn btn-danger btn-sm deleteWhite"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}
/********
 * end白名单过滤列表
 * ******
 */


/********
 * begin APP升级推送集合列表显示
 * ******
 */
//apk升级信息集合
//get_APP_Upgrade
function query_APP_Upgrade() {
  $('#get_APP_Upgrade').bootstrapTable ({
  	height:720,
  	search:true,
//  	showRefresh:true,
//  	showToggle:true,
  	showColumns:true,
      pagination: true,
      sidePagination: 'client',
      pageNumber: 1,
      pageSize: 20,
      pageList: [10, 20, 30, 50],
      toolbar: $('#toolbar'),
      uniqueId: 'id',
      url: 'get-App-Upgrade',
  });
}

window.upgradeEvents = {
        'click .deleteUpgrade': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-App-Upgrade",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#get_APP_Upgrade').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editUpgrade': function (e, value, row, index) {
        	$('#editUpgradeModel').modal('show');
        	$('#editUpgrade_id').val(row.id);
        	$('#editUpgrade_Name').val(row.name);
        	$('#editUpgrade_Android').val(row.android);
        	$('#edit_Description').val(row.description);
        	//填写安卓版本隐藏
        	/*$('#checkEditAndroid').hide();*/
        	
        	/*$('#checkEdit').attr('checked', false);*/
        	//判断商城推送按钮是否勾选,
    		if(row.type==1){
    			//显示填写安卓版本
    			$('#checkEditAndroid').show();
    			$('#checkEdit').attr('checked', true);
    			/*$('#checkEditAndroid').style.display='block';*/
    		}
    		//应用市场APP检测自己升级状态
    		if(row.type==-1){
    			//选中勾选框
    			$('#checkEditUpgrade').attr('checked', true);
    			/*$('#checkEditAndroid').style.display='block';*/
    		}
    		
        },
      //详情按钮
        'click .detailUpgrade': function (e, value, row, index) {
        	window.location.href="app-upgrade-detail.html?id=" + row.id;
        	
        }
}


//添加APP升级推送集合信息
function submit_addUpgradeClick() {
	if ($("#addUpgrade_Name").val()) {
		//勾选一种APP升级状态
		if(!($("#checkAdd").prop('checked') && $("#checkAddUpgrade").prop('checked'))){
			var type = 0;
			//判断商城推送按钮是否勾选,若勾选则这表apk属于商城推送
			if($("#checkAdd").prop('checked')){
				var type = 1;
			}
			//apk应用自动检测升级版本
			if($("#checkAddUpgrade").prop('checked')){
				var type = -1;
			}
			var data = {
				"name":$('#addUpgrade_Name').val(),
				"type": type,
				"android": $('#addUpgrade_Android').val(),
				"description": $('#add_Description').val()
			}
			$.ajax({
				type: "post",
				url: "check-UpgradeName",
				contentType : 'application/json',
				data: JSON.stringify(data),
				success: function(result) {
					if(result.code == 1){
						alert(result.msg);
						return;
					}else{
						$.ajax({
							type: "post",
							url: "add-App-Upgrade",
							contentType : 'application/json',
							data: JSON.stringify(data),
							success: function(result) {
								if(result.code == 1){
									$('#addUpgradeModel').modal('hide');
									layer.msg(result.msg);
									$('#get_APP_Upgrade').bootstrapTable('refresh');
								}
								if(result.code == -1){
									alert(result.msg);
								}
							},
							error: function(result, xhr) {
								console.log(result);
								console.log(xhr);
								layer.msg(result.msg,{icon:2});
							}
						});
					}
				},
				error: function(result, xhr) {
					console.log(result);
					console.log(xhr);
					layer.msg(result.msg,{icon:2});
				}
			});
		}else{
			layer.msg('Sorry, you can only choose one APP upgrade status.', {icon: 5});
		}
		
	} else {
		layer.msg('Please fill your name!', {icon: 5});
	}
}

//点击编辑提交按钮
$("#editUpgrade__Submit").click(function() {
 	if ($("#editUpgrade_Name").val()) {
 		//勾选一种APP升级状态
 		if(!($("#checkEdit").prop('checked') && $("#checkEditUpgrade").prop('checked'))){
 			var type = 0;
 			//判断商城推送按钮是否勾选,若勾选则这表apk属于商城推送
 			if($("#checkEdit").prop('checked')){
 				var type = 1;
 			}
 			//apk应用自动检测升级版本
 			if($("#checkEditUpgrade").prop('checked')){
 				var type = -1;
 			}
 	 		var data = {
 	 				"id":$('#editUpgrade_id').val(),
 	 				"name":$('#editUpgrade_Name').val(),
 	 				"type": type,
 	 				"android": $('#editUpgrade_Android').val(),
 	 				"description": $('#edit_Description').val()
 	 			}
 	 		
 	 		$.ajax({
 				type: "post",
 				url: "check-UpgradeName",
 				contentType : 'application/json',
 				data: JSON.stringify(data),
 				success: function(result) {
 					if(result.code == 1){
 						alert(result.msg);
 						return;
 					}else{
 						$.ajax({
 							type: "post",
 							url: "edit-App-Upgrade",
 							contentType : 'application/json',
 							data: JSON.stringify(data),
 							success: function(result) {
 								if(result.code == 1){
 									$('#editUpgradeModel').modal('hide');
 	 								layer.msg(result.msg);
 	 								$('#get_APP_Upgrade').bootstrapTable('refresh');
								}
								if(result.code == -1){
									alert(result.msg);
								}
 							},
 							error: function(result, xhr) {
 								console.log(result);
 								console.log(xhr);
 								layer.msg(result.msg,{icon:2});
 							}
 						});
 					}
 				},
 				error: function(result, xhr) {
 					console.log(result);
 					console.log(xhr);
 					layer.msg(result.msg,{icon:2});
 				}
 			});
		}else{
			layer.msg('Sorry, you can only choose one APP upgrade status.', {icon: 5});
		}
 		
	} else {
			layer.msg('Please fill your name!',{icon:2});
	}
});


//动态加载自动配置按钮
function upgradeOperationFun(value,row,index) {  
	return ['<button type="button" permission="upgrade:edit" class="btn btn-info btn-sm editUpgrade" data-toggle="modal" data-target="editUpgradeModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="upgrade:delete" class="btn btn-danger btn-sm deleteUpgrade"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="upgrade:detail" class="btn btn-info btn-sm detailUpgrade"><span class="glyphicon glyphicon-th"></span>Detail</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}

/********
 * end APP升级推送集合列表显示
 * ******
 */



/********
 * begin自动配置
 * ******
 */
//自动配置编辑和删除按钮
window.autoEvents = {
        'click .deleteAuto': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-AutoConfiguration",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#get_Network_table').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editAuto': function (e, value, row, index) {
        	$('#editAutoModel').modal('show');
        	$('#editAuto_id').val(row.id);
        	//机型版本编号
        	$('#editAutoModelVersion_id').val(row.modelVersionId);
        	//机型版本下拉框显示选中值
        	$('#editAuto_selectModelVersion').val(row.model+","+row.android+","+row.customer);
        	
        },
      //详情按钮
        'click .detailAuto': function (e, value, row, index) {
        	window.location.href="store-detail.html?id=" + row.id;
        	
        }
}


//添加自动配置信息
function submit_addAutoClick() {
	if ($("#selectAutoModelVersion").val()!=0) {
		var modelVersionId = $('#selectAutoModelVersion').val();
		var data = {
				"modelVersionId": modelVersionId,
				"appUpgradeId": $("#addAuto_selectAppNameModel").val(),
				"status": $('#addAuto_status').val()
			}
		$.ajax({
			type: "post",
			url: "check-AutoConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				//检测是否存在重复机型版本，不存在可以添加记录
				if(result.code==1){
					$.ajax({
						type: "post",
						url: "add-AutoConfiguration",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							$('#addAutoModel').modal('hide');
							layer.msg(result.msg);
							$('#get_Network_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.msg(result.msg,{icon:2});
						}
					});
				}else{
					alert(result.msg);
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
		layer.msg('Please select the model version!', {icon: 5});
	}

}

//点击自动配置编辑提交按钮
$("#editAuto_Submit").click(function() {
 	if ($("#editAuto_selectModelVersion").val()) {
 		var data = {
 				"id":$('#editAuto_id').val(),
 				"modelVersionId":$('#editAutoModelVersion_id').val(),
 				"appUpgradeId": $('#editAuto_selectAppNameModel').val(),
 				"status": $('#editAuto_status').val()
 			}
		$.ajax({
			type: "post",
			url: "update-AutoConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$('#editAutoModel').modal('hide');
				layer.msg(result.msg);
				$('#get_Network_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
			layer.msg('Please fill your model version!',{icon:2});
	}
});


//动态加载自动配置按钮
function autoOperationFun(value,row,index) {  
	return ['<button type="button" permission="auto:edit" class="btn btn-info btn-sm editAuto" data-toggle="modal" data-target="editProductModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="product:delete" class="btn btn-danger btn-sm deleteAuto"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="product:detail" class="btn btn-info btn-sm detailAuto"><span class="glyphicon glyphicon-th"></span>Detail</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}
/********
 * end自动配置
 * ******
 */


/********
 * begin静默安装
 * ******
 */
//静默安装编辑和删除按钮
window.staticEvents = {
        'click .deleteStatic': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-StaticConfiguration",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#getApp').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editStatic': function (e, value, row, index) {
        	$('#editStaticModel').modal('show');
        	$('#editStatic_id').val(row.id);
        	//机型版本编号
        	$('#editStaticModelVersion_id').val(row.modelVersionId);
        	//机型版本下拉框显示选中值
        	$('#editStatic_selectModelVersion').val(row.model+","+row.android+","+row.customer);
        	
        },
      //详情按钮
        'click .detailStatic': function (e, value, row, index) {
        	window.location.href="static-detail.html?id=" + row.id;
        	
        }
}


//添加静默安装信息
function submit_addStaticClick() {
	if ($("#selectStaticModelVersion").val()!=0) {
		var modelVersionId = $('#selectStaticModelVersion').val();
		var data = {
				"modelVersionId": modelVersionId,
				"appUpgradeId": $("#addStatic_selectAppNameModel").val(),
				"status": $('#addStatic_status').val()
			}
		$.ajax({
			type: "post",
			url: "check-StaticConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				//检测是否存在重复机型版本，不存在可以添加记录
				if(result.code==1){
					$.ajax({
						type: "post",
						url: "add-StaticConfiguration",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							$('#addStaticModel').modal('hide');
							layer.msg(result.msg);
							$('#getApp').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.msg(result.msg,{icon:2});
						}
					});
				}else{
					alert(result.msg);
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
		layer.msg('Please select the model version!', {icon: 5});
	}
}

//点击静默安装编辑提交按钮
$("#editStatic_Submit").click(function() {
 	if ($("#editStatic_selectModelVersion").val()) {
 		var data = {
 				"id":$('#editStatic_id').val(),
 				"modelVersionId":$('#editStaticModelVersion_id').val(),
 				"appUpgradeId": $('#editStatic_selectAppNameModel').val(),
 				"status": $('#editStatic_status').val()
 			}
		$.ajax({
			type: "post",
			url: "update-StaticConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$('#editStaticModel').modal('hide');
				layer.msg(result.msg);
				$('#getApp').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
			layer.msg('Please fill your model version!',{icon:2});
	}
});


//动态加载静默安装按钮
function staticOperationFun(value,row,index) {
	return ['<button type="button" permission="static:edit" class="btn btn-info btn-sm editStatic" data-toggle="modal" data-target="editStaticModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="static:delete" class="btn btn-danger btn-sm deleteStatic"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="static:detail" class="btn btn-info btn-sm detailStatic"><span class="glyphicon glyphicon-th"></span>Detail</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}

/********
 * end静默安装
 * ******
 */


/********
 * begin商城APP配置
 * ******
 */
//商城APP配置编辑和删除按钮
window.storeEvents = {
        'click .deleteStore': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-StoreConfiguration",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#get_AppStore_table').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editStore': function (e, value, row, index) {
        	$('#editStoreModel').modal('show');
        	$('#editStore_id').val(row.id);
        	//机型版本编号
        	$('#editModelVersion_id').val(row.modelVersionId);
        	//机型版本下拉框显示选中值
        	$('#editStore_selectModelVersion').val(row.model+","+row.android+","+row.customer);
        	
        },
      //详情按钮
        'click .detailStore': function (e, value, row, index) {
        	window.location.href="app-store-detail.html?id=" + row.id;
        	
        }
}


//添加商城APP配置信息
function submit_addStoreClick() {
	if ($("#addStore_selectModelVersion").val()!=0) {
		var modelVersionId = $('#addStore_selectModelVersion').val();
		var data = {
				"modelVersionId": modelVersionId,
				"appUpgradeId": $("#addStore_selectAPPName").val(),
				"status": $('#addStore_status').val()
			}
		$.ajax({
			type: "post",
			url: "check-StoreConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				//检测是否存在重复机型版本，不存在可以添加记录
				if(result.code==1){
					$.ajax({
						type: "post",
						url: "add-StoreConfiguration",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							layer.msg(result.msg);
							$('#addAppStoreModel').modal('hide');
							$('#get_AppStore_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.msg(result.msg,{icon:2});
						}
					});
				}else{
					alert(result.msg);
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
		layer.msg('Please select the model version!', {icon: 5});
	}
}

//点击商城APP配置编辑提交按钮
$("#editStore_Submit").click(function() {
 	if ($("#editStore_selectModelVersion").val()) {
 		var data = {
 				"id":$('#editStore_id').val(),
 				"modelVersionId":$('#editModelVersion_id').val(),
 				"appUpgradeId": $('#editStore_selectAPPName').val(),
 				"status": $('#editStore_status').val()
 			}
		$.ajax({
			type: "post",
			url: "edit-StoreConfiguration",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$('#editStoreModel').modal('hide');
				layer.msg(result.msg);
				$('#get_AppStore_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
	} else {
			layer.msg('Please select the model version!',{icon:2});
	}
});


//动态加载商城APP配置按钮
function storeOperationFun(value,row,index) {  
	return ['<button type="button" permission="store:edit" class="btn btn-info btn-sm editStore" data-toggle="modal" data-target="editProductModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="store:delete" class="btn btn-danger btn-sm deleteStore"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="store:detail" class="btn btn-info btn-sm detailStore"><span class="glyphicon glyphicon-th"></span>Detail</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}
/********
 * end商城APP配置
 * ******
 */



/********
 * begin用于OTA升级xml过滤Mac
 * ******
 */
//OTA升级Mac过滤拦截编辑和删除按钮
window.xmlStatusEvents = {
        'click .deleteXmlFilter': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
	      		  time: 0 //不自动关闭
	      		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-XmlFilter",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#find_ping_table').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result.msg);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        //编辑按钮
        'click .editXmlFilter': function (e, value, row, index) {
        	$('#editXmlFilterModel').modal('show');
        	$('#editXmlFilter_id').val(row.id);
        	$('#editBeginMac').val(row.beginmac);
        	$('#editEndMac').val(row.endmac);
        	$('#editXmlFilter_status').val(row.status);
        }
     
}


//添加OTA升级Mac过滤拦截信息
function submit_addXmlFilterClick() {
	var beginmac = $('#addBeginMac').val();
	var	endmac = $("#addEndMac").val();
	var status = $('#addXmlFilter_status').val();
	
	//只填写了开始SN，没填写结束SN的情况
	if(beginmac){
		if(!endmac){
			alert("You did not fill in the ending SN.");
			$("#addEndMac").focus();
			return;
		}
		var reg = /^[0-9a-fA-F]{1,12}$/;
		if(!reg.test(endmac) || !reg.test(beginmac)){ 
			alert("The SN serial number you enter must be 12 hexadecimal values.");
			return;
		}
		var NumBeginSN = parseInt(beginmac, 16);
		var NumEndSN = parseInt(endmac, 16);
		var diffSN = NumEndSN - NumBeginSN;
		if(diffSN < 0) {
			alert("SN should begin with small and end with big.");
			return;
		}
	}	
	//只填写了结束SN，没填写开始SN的情况
	if(endmac){
		if(!beginmac){
			alert("You didn't fill in the starting SN.");
			$("#addBeginMac").focus();
			return;
		}
		var reg = /^[0-9a-fA-F]{1,12}$/;
		if(!reg.test(endmac) || !reg.test(beginmac)){ 
			alert("The SN serial number you enter must be 12 hexadecimal values");
			return;
		}
		var NumBeginSN = parseInt(beginmac, 16);
		var NumEndSN = parseInt(endmac, 16);
		var diffSN = NumEndSN - NumBeginSN;
		if(diffSN < 0) {
			alert("SN should begin with small and end with big");
			return;
		}
	}	
	var data = {
			"beginmac":beginmac,
			"endmac": endmac,
			"status": status
		}

		$.ajax({
			type: "post",
			url: "add-XmlFilter",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$('#addXmlFilterModel').modal('hide');
				layer.msg(result.msg);
				$('#find_ping_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg(result.msg,{icon:2});
			}
		});
}

//点击OTA升级Mac过滤拦截信息，编辑提交按钮
$("#editXmlFilter_Submit").click(function() {
	var beginmac = $('#editBeginMac').val();
	var	endmac = $("#editEndMac").val();
	
	//只填写了开始SN，没填写结束SN的情况
	if(beginmac){
		if(!endmac){
			alert("You did not fill in the ending SN.");
			$("#editEndMac").focus();
			return;
		}
		var reg = /^[0-9a-fA-F]{1,12}$/;
		if(!reg.test(endmac) || !reg.test(beginmac)){ 
			alert("The SN serial number you enter must be 12 hexadecimal values.");
			return;
		}
		var NumBeginSN = parseInt(beginmac, 16);
		var NumEndSN = parseInt(endmac, 16);
		var diffSN = NumEndSN - NumBeginSN;
		if(diffSN < 0) {
			alert("SN should begin with small and end with big.");
			return;
		}
	}	
	//只填写了结束SN，没填写开始SN的情况
	if(endmac){
		if(!beginmac){
			alert("You didn't fill in the starting SN.");
			$("#editBeginMac").focus();
			return;
		}
		var reg = /^[0-9a-fA-F]{1,12}$/;
		if(!reg.test(endmac) || !reg.test(beginmac)){ 
			alert("The SN serial number you enter must be 12 hexadecimal values");
			return;
		}
		var NumBeginSN = parseInt(beginmac, 16);
		var NumEndSN = parseInt(endmac, 16);
		var diffSN = NumEndSN - NumBeginSN;
		if(diffSN < 0) {
			alert("SN should begin with small and end with big");
			return;
		}
	}
	var data = {
				"id":$('#editXmlFilter_id').val(),
				"beginmac":$('#editBeginMac').val(),
				"endmac": $('#editEndMac').val(),
				"status": $("#editXmlFilter_status").val()
			}
	$.ajax({
		type: "post",
		url: "edit-XmlFilter",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			$('#editXmlFilterModel').modal('hide');
			layer.msg(result.msg);
			$('#find_ping_table').bootstrapTable('refresh');
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
			layer.msg(result.msg,{icon:2});
		}
	});
});


//动态加载OTA升级xml过滤Mac按钮
function xmlStatusOperationFun(value,row,index) {
	return ['<button type="button" permission="xmlStatus:edit" class="btn btn-info btn-sm editXmlFilter" data-toggle="modal" data-target="editXmlFilterModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="xmlStatus:delete" class="btn btn-danger btn-sm deleteXmlFilter"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}

//Mac拦截状态显示信息
function xmlFilterStatusFun(value,row,index) {
	//open是1 close是-1
	if(value=="-1") {
		return '<font color="red">Close</font>';
	}
	if(value=="1") {
		return '<font color="green">Open</font>';
	}
	return '-';
}

/********
 * end用于OTA升级xml过滤Mac
 * ******
 */


//用于判断是否显示动态按钮
function aaa(){
	// bottons control
	$("button[permission]").each(function(index, event) {
		var permission = $(event).attr("permission");
		$(event).show();
		/*if(checkUserBotton(permission)) {
		}*/
		
	});
	// end
}

//状态显示信息
function productStatusFun(value,row,index) {
	//open是1 close是0
	if(value=="0") {
		return '<font color="red">Close</font>';
	}
	if(value=="1") {
		return '<font color="green">Open</font>';
	}
	return '-';
}


//弹出上传Model
$("#activeUpload").click(function(){
	$("#uploadEventFile").val("");
	$("#progressBar").width("0%");
	$("#progressBar").parent().hide();
    $("#uploadModal").modal("show");
})

function base_selectWithFilterClick() {
    /*$('#getApp').bootstrapTable('refresh');*/
}

function app_selectWithFilterClick() {
    $('#getApp').bootstrapTable('refresh');
}
function find_selectWithFilterClick() {
    $('#find_ping_table').bootstrapTable('refresh');
}

//刷新崩溃日志
function log_selectWithFilterClick(thisBtn) {
	$('#getLogFile_table').bootstrapTable('refresh');
}

var clock = '';
var nums = 5;
var btn;

//向终端发起请求，抓起崩溃日志
function sendCode(thisBtn)
{ 
	//Mac地址
	var mac = $('#device_mac').text();
	//选择抓取日志的时间
	var time = $('#time').val();
	if(time==null || time == "0"){
		alert("Please input the correct time");
		return;
	};
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/ 
	if (!re.test(time)) {
		alert("Please input the correct time");
		return;
	}
	time = time*60; 
	
	sendLog(mac,time);
	
	 nums = nums+parseInt(time);
	 btn = thisBtn;
	 btn.disabled = true; //将按钮置为不可点击
	 clock = setInterval(doLoop, 1000); //一秒执行一次
}
function doLoop()
{
	 nums--;
	 document.getElementById('show').innerHTML="Remaining "+nums+" seconds refresh"; // 显示倒计时 
	 if(nums > 0){
		
	 }else{
		  document.getElementById('show').innerHTML="";
		  clearInterval(clock); //清除js定时器
		  btn.disabled = false;
		  nums = 5; //重置时间
		  $('#getLogFile_table').bootstrapTable('refresh');
	 }
} 


function baseDeviceClick(){
}

function networkDeviceClick(){
	query_network();
}

//自动配置信息
//get_network
function query_network() {
    $('#get_Network_table').bootstrapTable ({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'getAutoConfiguration',
    });
}
/**
 * //APP商城配置信息
 * @returns
 */
//get_AppStore_table
function query_AppStore_table() {
  $('#get_AppStore_table').bootstrapTable ({
  	height:720,
  	search:true,
//  	showRefresh:true,
//  	showToggle:true,
  	showColumns:true,
	pagination: true,
	sidePagination: 'client',
	pageNumber: 1,
	pageSize: 20,
	pageList: [10, 20, 30, 50],
	toolbar: $('#toolbar'),
	uniqueId: 'id',
	url: 'getStoreConfiguration',
  });
}

function refreshData(){
}

function getapplist(){
	queryApp();
}


/**
 * 自动配置查询机型版本下拉框
 * @returns
 */
function queryAutoModelList() {
    $.ajax({
        type: "get",
        url: "getModelVersion",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
                $('#selectAutoModelVersion').append("<option value='" + result[i].id + "'>" + result[i].model + "," + result[i].android + "," + result[i].customer + "</option>");
            }
            $('#selectAutoModelVersion').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

/**
 *自动配置查询APP升级列表下拉框
 * @returns
 */
function queryAutoAPPNameList() {
    $.ajax({
        type: "get",
        url: "check-Auto-Static-AppNameList",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
            	//添加时，弹APP升级下拉框
                $('#addAuto_selectAppNameModel').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
                //编辑时，弹APP升级下拉框
                $('#editAuto_selectAppNameModel').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
            }
            $('#addAuto_selectAppNameModel').selectpicker('refresh');
            $('#editAuto_selectAppNameModel').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

/**
 * 静默安装查询机型版本下拉框
 * @returns
 */
function queryStaticModelList() {
    $.ajax({
        type: "get",
        url: "getModelVersion",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
                $('#selectStaticModelVersion').append("<option value='" + result[i].id + "'>" + result[i].model + "," + result[i].android + "," + result[i].customer + "</option>");
            }
            $('#selectStaticModelVersion').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

/**
 *静默安装查询APP升级列表下拉框
 * @returns
 */
function queryStaticAPPNameList() {
    $.ajax({
        type: "get",
        url: "check-Auto-Static-AppNameList",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
            	//添加时，弹APP升级下拉框
                $('#addStatic_selectAppNameModel').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
                //编辑时，弹APP升级下拉框
                $('#editStatic_selectAppNameModel').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
            }
            $('#addStatic_selectAppNameModel').selectpicker('refresh');
            $('#editStatic_selectAppNameModel').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

/**
 * 添加APP商城查询机型版本下拉框
 * @returns
 */
function queryAppStoreModelList() {
    $.ajax({
        type: "get",
        url: "getModelVersion",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
            	//添加机型版本下拉框
                $('#addStore_selectModelVersion').append("<option value='" + result[i].id + "'>" + result[i].model + "," + result[i].android + "," + result[i].customer + "</option>");
            }
            $('#addStore_selectModelVersion').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}


/**
 * APP商城查询APP升级列表下拉框
 * @returns
 */
function queryAppStoreAPPNameList() {
    $.ajax({
        type: "get",
        url: "check-AppStoreList",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
            	//添加时，弹APP升级下拉框
                $('#addStore_selectAPPName').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
                //编辑时，弹APP升级下拉框
                $('#editStore_selectAPPName').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
            }
            $('#addStore_selectAPPName').selectpicker('refresh');
            $('#editStore_selectAPPName').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

function confirm_selectDeviceWithFilterClick() {
    $('#table_boxinfo').bootstrapTable('refresh');
}

/*
 *       queryBoxList():加载页面时，加载Box信息的函数
 * */
function queryBoxList() {
    $('#table_boxinfo').bootstrapTable({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'deviceinfo',
    
    });
}


function showContent(val) {
  	$.ajax({
          type: "post",
          url: "find_BoxState",
          contentType: 'application/json',
          data: JSON.stringify({mac: val}),
          success: function (result) {
              var boxState = result.box.state;
              
              //状态为true，说明已经阻止盒子连接服务器[会弹出是否取消阻止连接]
              if (boxState == true) {
            	  layer.msg('The box has prevented from being blocked.<br/>Sure to Relieve?', {
            		  time: 0 //不自动关闭
            		  ,btn: ['Confirm', 'Cancel']
            		  ,yes: function(index){
          		    	$.ajax({
          		            type: "post",
          		            url: "update_BoxState",
          		            contentType: 'application/json',
          		            data: JSON.stringify({mac: val,state:0}),
          		            success: function (result) {
          		                if (result.code == 1) {
          		                    layer.msg("Relieve Successfully!");
          		                    $('#table_boxinfo').bootstrapTable('refresh');
          		                }else{
          		                	layer.msg("Operation failed!");
          		                }
          		            },
          		            error: function (result, xhr) {
          		                console.log(result);
          		                console.log(xhr);
          		            }
          		        })
          	        
            		  }
            	});
                  
              }else{
            	  layer.msg('Sure to Blocking?', {
            		  time: 0 //不自动关闭
            		  ,btn: ['Confirm', 'Cancel']
            		  ,yes: function(index){
          		    	$.ajax({
          		            type: "post",
          		            url: "update_BoxState",
          		            contentType: 'application/json',
          		            data: JSON.stringify({mac: val,state:1}),
          		            success: function (result) {
          		                if (result.code == 1) {
          		                    layer.msg("Blocking Successfully!");
          		                    $('#table_boxinfo').bootstrapTable('refresh');
          		                }else{
          		                	layer.msg("Operation failed!");
          		                }
          		            },
          		            error: function (result, xhr) {
          		                console.log(result);
          		                console.log(xhr);
          		            }
          		        })
          	        
            		  }
            	});
                  
              }
          },
          error: function (result, xhr) {
              console.log(result);
              console.log(xhr);
          }
      })
	
}


function statusFun(value,row,index) {
	if(value=="Online") {
		return '<font color="green">'+'Online'+'</font>';
	} else if(value="Offline") {
		return '<font color="red">'+'Offline'+'</font>';
	}
	
}




/*
 *      $('#addDeviceModal').on('shown.bs.modal', function ():添加Device的模态框shown时的动作
 *      addDeviceselectCustomerList():添加Device的模态框中的Customer选择器变动时的操作
 *      queryAddDeviceCustomerList():添加Device的模态框中的CUstomer选择器
 *      queryAddDeviceModelList():添加Device的模态框中的Model选择器
 *      queryGroupforAddDevice():添加Device的模态框中的Group选择器
 *      $("#submitAddDevice").click(function ())：确认添加Deivce的操作
 * */
$('#addDeviceModal').on('shown.bs.modal', function () {
    // 执行一些动作...
    //alert("coming soon");
    queryGroupforAddDevice();
})
function addDeviceSelectCustomerOnchange() {
//获取被选中的option标签选项
//     alert($('#selectCustomer').val());
    queryGroupforAddDevice();
}
$('#uploadModal').on('shown.bs.modal', function () {
    // 执行一些动作...
    //alert("coming soon");
	queryGroupforAddExcel();
})
function addExcelSelectCustomerOnchange() {
	queryGroupforAddExcel();
}

function factoryResetDeviceClick() {

	var selected = JSON.stringify($('#table_boxinfo').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('Sure to reset?', {
  		  time: 0 //不自动关闭
  		  ,btn: ['Confirm', 'Cancel']
  		  ,yes: function(index){
  		    layer.close(index);
  		    for (var i = 0; i < selected.length; i++) {
  	    		var mac = selected[i].mac;
  	    		sendReset(mac);
  	        }
  		  }});
    	
    } else {
    	layer.msg('请选择一条记录!',{icon: 5});
    }

}
function selectExcelTemp(){
	var htmlOut = '<div id="main">'
		+'<div class="maindiv">'
			+'<div class="mainbody">'
				+'<div class="wzspzy">'
				+'<div class="left">'
				+'<a href="#PICFELE#" target="_blank"> <img src="#IMGFILE#"'
				+'alt="#TITLE#" class="mbimg"></a>'
				+'</div>'
					+'<div class="right" style="text-align: left;">'	
						+'<div class="mbtitle">'
						+'<h1> <strong>#TEMPTITLE#</strong> </h1>'
					+'</div>'
	
						+'<div class="fenlei">'
							+'<ul>'
								+'<li>AppSoftware：#APPSOF#</li>'
								+'<li>Authorize：#FREETEMP#</li>'
								+'<li>Type：<span class="mbExcel">#EXCLTEMP#</span></li>'
								+'<li>FileExtension：#FILEEXT#</li>'
								+'<li>UpdateTime：#DATE#</li>'
								+'<li></li>'
							+'</ul>'
						+'</div>'
						+'<div class="mbxzan">'
							+'<a rel="nofollow"'
								+'href="#EXCLFILE#"'
								+'target="_blank">Free Download</a>'
						+'</div>'
	
					+'</div>'
				+'</div>'
			+'</div>'
		+'</div>'
	+'</div>';
	
	
	var html = '';
	$.post("selectExcelTemp", function(data) {
		if(data == null || data == undefined || data == "") alert("沒有添加模板信息！");
		var templist = $.parseJSON(data);
		
		for (var i = 0; i < templist.length; i++) {
			html += htmlOut.replace("#TITLE#",templist[i].exceltempname).replace("#TEMPTITLE#", templist[i].exceltempname)
			.replace("#IMGFILE#", templist[i].picturefile).replace("#EXCLFILE#", templist[i].exceltempfile).replace("#PICFELE#", templist[i].picturefile)
			.replace("#APPSOF#", templist[i].appsoftware).replace("#FREETEMP#", templist[i].useauthorization)
			.replace("#EXCLTEMP#", templist[i].temptype).replace("#FILEEXT#", templist[i].fileextension)
			.replace("#DATE#", jsonDateFormat2(templist[i].updatetime));
		}
		$("#exltemp").html(html);
	
	});

}

//时间转换成yyyy-mm-dd
function jsonDateFormat2(jsonDate) {
	if(jsonDate == null) return "-";
    //json日期格式转换为正常格式
    var jsonDateStr = jsonDate.toString();//此处用到toString（）是为了让传入的值为字符串类型，目的是为了避免传入的数据类型不支持.replace（）方法
    try {
        var k = parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10);
        if (k < 0) return null;

        var date = new Date(parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var milliseconds = date.getMilliseconds();
        return date.getFullYear() + "-" + month + "-" + day;
    }
    catch (ex) {
        return "时间格式转换错误";
    }
    
}

//
//白名单显示js调试
//
/*filtering.html的数据加载*/
function detailAdPageLoad() {
    $('#ref_data_table').bootstrapTable ({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'getFiltering',
    });
}

/**
 * json时间戳格式转换格式
 * @param jsonDate
 * @returns
 */
function jsonDateFormat(jsonDate) {
	if(jsonDate == null) return "-";
    //json日期格式转换为正常格式
    var jsonDateStr = jsonDate.toString();//此处用到toString（）是为了让传入的值为字符串类型，目的是为了避免传入的数据类型不支持.replace（）方法
    try {
        var k = parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10);
        if (k < 0) return null;

        var date = new Date(parseInt(jsonDateStr.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        var milliseconds = date.getMilliseconds();
        return date.getFullYear() + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    }
    catch (ex) {
        return "时间格式转换错误";
    }
    
}
/*Excel模板页面加载*/
function excelTempClick() {
	window.location.href="excelTemp.html";
}

/*device-detail.html页面加载*/
function detailAdResetDeviceClick() {
	var selected = JSON.stringify($('#table_boxinfo').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
    	window.location.href="device-detail.html?mac=" + selected[0].mac;
    } else {
        layer.msg("请选择一条记录!",{icon:5});
    }

}
function restartDeviceClick() {

    var selected = JSON.stringify($('#table_boxinfo').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('Sure to restart?', {
  		  time: 0 //不自动关闭
  		  ,btn: ['Confirm', 'Cancel']
  		  ,yes: function(index){
  		    layer.close(index);
  	    	for (var i = 0; i < selected.length; i++) {
  	    		var mac = selected[i].mac;
  	    		sendReboot(mac);
  	        }
  		  }});
    	
    } else {
    	layer.msg('请选择一条记录!',{icon: 5});
    } 

}
$("#submitAddDevice").click(function () {
	
    var MACBegin = $("#MACBegin").val();
    var MACEnd = $("#MACEnd").val();
    var UserIDBegin = $("#UserIDBegin").val();
    var UserIDEnd = $("#UserIDEnd").val();
    var Customer = $("#addDevice_selectCustomer").val();
    var Group = $("#addDevice_selectGroup").val();
    var Model = $("#addDevice_selectModel").val();
    
    if (MACBegin && MACEnd && UserIDBegin && UserIDEnd) {//先判断填写的表单是否为空
        if (checkMACAddress(MACBegin) && checkMACAddress(MACEnd)) {//判断MAC地址是否合法
            //去掉冒号再转换成16进制
        	var Num0xMACBegin = parseInt(MACBegin.replace(/:/g, ""), 16);
            var Num0xMACEnd = parseInt(MACEnd.replace(/:/g, ""), 16);
            var diffMAC = Num0xMACEnd - Num0xMACBegin;
            //var diffUserID = UserIDEnd - UserIDBegin;
          
            //序列号转换成16进制
            var usrbeginid = parseInt(UserIDBegin, 16);
            var usrendid = parseInt(UserIDEnd, 16);
            var diffUserID = usrendid-usrbeginid;
            
            if (diffMAC != diffUserID) {//判断MAC地址和UserID的间隔个数是否相同
                console.log(Num0xMACEnd - Num0xMACBegin);
                console.log(UserIDEnd - UserIDBegin);
                layer.msg("用户ID和MAC地址之间的计数不正确!",{icon:2});
            } else if (diffMAC < 0 || diffUserID < 0) {
                layer.msg("MAC地址或SN序列号应以小号开头，以大号结尾",{icon:2});
            } else {
                var count = UserIDEnd - UserIDBegin;
                var data = {
                    "count": count,
                    "MACBegin": MACBegin,
                    "MACEnd": MACEnd,
                    "UserIDBegin": UserIDBegin,
                    "UserIDEnd": UserIDEnd,
                    "Customer": Customer,
                    "Group": Group,
                    "Model": Model,
                };
                
                $.ajax({
                    contentType: 'application/json',
                    type: "post",
                    url: "submitBox",
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    error: function (result) {
                        alert("对不起，添加信息失败!");
                        console.log(result);
                    },
	                success: function (result) {
//	                	result = $.parseJSON(result);
	                	if(result.code == 1){
	                		layer.msg("操作成功，盒子信息录入完成!");
	                		$("#MACBegin").val("");
	                		$("#MACEnd").val("");
	                		$("#UserIDBegin").val("");
	                		$("#UserIDEnd").val("");
	                		$('#addDeviceModal').modal('hide');
	                		$('#table_boxinfo').bootstrapTable('refresh',result);
	                		console.log(result);
	                	}else if(result.code == -1){
	                		alert("对不起，添加信息失败!");
	                        console.log(result);
	                	}else{
	                		alert("出现未知错误!");
	                	}
	                }
                });
            }
        } else {
            layer.msg("MAC地址是非法的！",{icon:2});
        }
    } else {//让空的表单获取焦点
        if (!MACBegin) {
            layer.msg("你未填写开始MAC地址",{icon:2});
            $("#MACBegin").focus();
        } else if (!MACEnd) {
            layer.msg("你未填写结束MAC地址",{icon:2});
            $("#MACEnd").focus();
        } else if (!UserIDBegin) {
            layer.msg("你未填写开始SN序列号",{icon:2});
            $("#UserIDBegin").focus();
        } else {
            layer.msg("你未填写结束SN序列号",{icon:2});
            $("#UserIDEnd").focus();
        }
    }
    return false;
})
function checkMACAddress(address) {
    if (address.length < 17) {
        return false;
    } else {
        var reg = /([a-fA-F0-9]{2}:){5}[a-fA-F0-9]{2}/;
        if (!address.match(reg)) {
            return false;
        }
    }
    return true;
}
/*
 *
 *       deleteDeviceClick():点击delete时，删除对应的记录
 *
 * */
function deleteDeviceClick() {
    var selected = JSON.stringify($('#table_boxinfo').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('确定删除这条信息?', {
    		  time: 0 //不自动关闭
    		  ,btn: ['确定', '取消']
    		  ,yes: function(index){
    		    layer.close(index);
    		    for (var i = 0; i < selected.length; i++) {
    	            $.ajax({
    	                type: "post",
    	                url: "delete-Box",
    	                contentType: 'application/json',
    	                data: JSON.stringify({mac: selected[i].mac}),
    	                success: function (result) {
//    	                    result = $.parseJSON(result);
    	                    if (result.code == 1) {
    	                        layer.msg("删除成功!");
    	                        $('#table_boxinfo').bootstrapTable('refresh');
    	                    }
    	                },
    	                error: function (result, xhr) {
    	                    console.log(result);
    	                    console.log(xhr);
    	                }
    	            })
    	        }
    	}});

    	
    } else {
    	layer.msg('222',{icon: 5});
    }
    

}
/*
 *      编辑Device时用到的模态框及相关函数
 *
 *
 * */
$('#editDeviceModal').on('show.bs.modal', function () {
    // 执行一些动作...
    //alert("coming soon");
    var selected = JSON.stringify($('#table_boxinfo').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
        $('#editDevice_selectedMac').val(selected[0].mac);
        $("#editDevice_selectedMac").attr("readOnly", true);
        $('#editDevice_selectedUserID').val(selected[0].sn);
        $('#editDevice_selectedVserion').val(selected[0].romversion);
    } else {
        layer.msg("请选择一条记录!",{icon:2});
        $('#editDeviceModal').modal('hidden');
    }
})



function submitEditDeviceClick() {
    data = {
        mac: $('#editDevice_selectedMac').val(),
        userid: $('#editDevice_selectedUserID').val(),
        romversion: $('#editDevice_selectedVserion').val(),
        customer: $('#editDevice_selectCustomer').val(),
        group: $('#editDevice_selectGroup').val(),
        model: $('#editDevice_selectModel').val(),
    };
    $.ajax({
        type: "post",
        url: "edit-Box",
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
//            result = $.parseJSON(result);
            if (result.code == 1) {
            	$('#editDeviceModal').modal('hide');
            	layer.msg('Success!');
                $('#table_boxinfo').bootstrapTable('refresh');
            }
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    })
}


function timeFormatter(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}

function apkdownloadFormatter(value,row,index) {
	return '<a target="_blank" href='+ value+'>'+value+'</a>';
}



/**
 * 显示静默安装过滤信息
 */
function queryApp() {
	$('#getApp').bootstrapTable ({
		height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'getStaticConfiguration',
    });
	
}

/**
 * 显示XML过滤信息
 */
function queryPing() {
	$('#find_ping_table').bootstrapTable ({
		height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'getXmlFilter',
    });
	
}

/**
 * 显示log信息
 */
function queryLogs() {
	$('#getLogFile_table').bootstrapTable ({
    	height:620,
    	/*search:true,*/
//    	searchAlign:'left',
//    	showRefresh:true,
//    	showToggle:true,
    	//showColumns:true,
    	showExport:true,
//    	exportTypes:['excel'],
    	exportDataType:'all',
    	exportOptions:{  
//    		ignoreColumn: [0,1],  //忽略某一列的索引  
            fileName: 'log',  //文件名称设置  
            worksheetName: 'log',  //表格工作区名称  
//            tableName: 'applog',  
            excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
//            onMsoNumberFormat: DoOnMsoNumberFormat  
        }, 
        pagination: true,
        sidePagination: 'server', //client
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
//        toolbar: $('#toolbar'),
        uniqueId: 'id',
        queryParams: function (params) {
            return {
            	boxId: $("#device_mac").text(),
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'getLoglsit',
    });
	
}

var User = function(){  
    this.init = function(){ 
        //模拟上传excel  
         $("#uploadEventBtn").unbind("click").bind("click",function(){  
             $("#uploadEventFile").click();  
         });  
         $("#uploadEventFile").bind("change",function(){  
             $("#uploadEventPath").attr("value",$("#uploadEventFile").val());  
         });
         //点击上传按钮
         $("#uploadBtn").unbind("click").bind("click",function(){
        	 //判断机型不为空
        	 if ($("#importWhite_Model").val()) {
        		 var uploadEventFile = $("#uploadEventFile").val();  
                 if(uploadEventFile == ''){  
                     alert("Please select the excel file and import again!");  
                 }else if(uploadEventFile.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel  
                     alert("Only import Excel files is supported!");  
                 }else{
                	 // 进度条归零
            	    $("#progressBar").width("0%");
            	    // 进度条显示
            	    $("#progressBar").parent().show();
            	    $("#progressBar").parent().addClass("active");
            	    var fileSplit = $("#uploadEventFile").val().split('.');
            	    var length = fileSplit.length;
            	    $(this).attr("disabled", true);
                     var url =  'importBWList';  
                     var formData = new FormData($("#batchUpload")[0]);  
                     user.sendAjaxRequest(url,'POST',formData);  
                 } 
        	 }else {
     			layer.msg('Please fill your model name!',{icon:2});
        	}
        	 
         }); 
          
    };  
  
    this.sendAjaxRequest = function(url,type,data){  
        $.ajax({  
            url : url,  
            type : type,  
            data : data,
            xhr: function(){ //获取ajaxSettings中的xhr对象，为它的upload属性绑定progress事件的处理函数
                myXhr = $.ajaxSettings.xhr();
                if(progressFunction && myXhr.upload) { //检查进度函数和upload属性是否存在
                    //绑定progress事件的回调函数
                    myXhr.upload.addEventListener("progress",progressFunction, false);
                }
                return myXhr; //xhr对象返回给jQuery使用
            },
            success : function(result) {
            	if(result.code==1){
            		layer.alert(result.msg);
            		$("#alreadyUpload").val("1");
            		$("#uploadBtn").attr("disabled", false);
            		$("#uploadBtn").val("Upload");
            		$("#uploadModal").modal("hide");
            		$('#ref_data_table').bootstrapTable('refresh');
            	}else{
            		layer.alert(result.msg);
            		//进度条隐藏
            		document.getElementById('probar').style.display='none';
            		$("#alreadyUpload").val("0");
            		$("#uploadBtn").attr("disabled", false);
            		$("#uploadBtn").val("Upload");
            	}
            },  
            error : function() {  
            	layer.alert("Excel file import failure！");
                $("#alreadyUpload").val("0");
                $("#uploadBtn").attr("disabled", false);
                $("#uploadBtn").val("Upload");
                $("#uploadModal").modal("hide");
            },  
            cache : false,  
            contentType : false,  
            processData : false  
        });
        
    };  
} 


//文件修改时
$("#uploadEventFile").change(function() {
    $("#uploadBtn").val("Upload");
    $("#progressBar").width("0%");
    var file = $(this).prop('files');
    if (file.length != 0) {
        $("#batchUploadBtn").attr('disabled', false);
    }

});

//文件修改时
$("#uploadEventFile").change(function() {
    $("#uploadBtn").val("Upload");
    var file = $(this).prop("files");
    if (file.length != 0) {
        $("#uploadBtn").attr("disabled", false);
    }
});

//进度条控制
function progressFunction(evt) {
	var progressBar = $("#progressBar");
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#uploadBtn").val("Uploading：" + completePercent);
        progressBar.width(completePercent);
    }
}
