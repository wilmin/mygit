$(document).ready(function () {
	queryCustomerList();
	queryModelList();
	queryapplog();
	$('#confirm_selectAppLogWithFilter').bind("click", confirm_selectAppLogWithFilterClick);
});
function actionReFormFun(value,row,index) {
	if(value=="Downloading") {
		return '<font color="blue">Downloading</font>';
	} else if(value=="Install Success") {
		return '<font color="green">Install Success</font>';
	} else if(value=="Download Fail") {
		return '<font color="red">Download Fail</font>';
	} if(value=="Install Fail") {
		return '<font color="red">Install Fail</font>';
	} if(value=="Download Success"){
		return '<font color="green">Download Success</font>';
	}
}

function confirm_selectAppLogWithFilterClick() {
    $('#applog_table').bootstrapTable('refresh');
}
function DoOnMsoNumberFormat(cell, row, col) {  
    var result = "";  
    if (row > 0 && col == 0)  
        result = "\\@";  
    return result;  
} 
function queryapplog() {
	$('#applog_table').bootstrapTable ({
    	height:720,
//   	search:true,
//    	searchAlign:'left',
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
    	showExport:true,
//    	exportTypes:['excel'],
    	exportDataType:'all',
    	exportOptions:{  
//    		ignoreColumn: [0,1],  //忽略某一列的索引  
            fileName: 'applog',  //文件名称设置  
            worksheetName: 'applog',  //表格工作区名称  
//            tableName: 'applog',  
            excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
//            onMsoNumberFormat: DoOnMsoNumberFormat  
        }, 
        pagination: true,
        sidePagination: 'server',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
//        toolbar: $('#toolbar'),
        uniqueId: 'id',
        queryParams: function (params) {
            return {
            	keyword: $('#keyword').val(),
                customer: $('#selectCustomer').val(),
                model: $('#selectModel').val(),
                action: $('#selectAction').val(),
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'applog-info',
    });
}

function queryCustomerList() {
    $.ajax({
        type: "get",
        url: "customer-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectCustomer').append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
            }
            $('#selectCustomer').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

function queryModelList() {
    $.ajax({
        type: "get",
        url: "model-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
            	if(data[i].status == 0) continue;
                $('#selectModel').append("<option value='" + data[i].type + "'>" + data[i].type + "</option>");
            }
            $('#selectModel').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}
