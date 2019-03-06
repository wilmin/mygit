$(document).ready(function () {
	queryuserList();
	queryserverlog();
	$('#confirm_selectWithFilter').bind("click", confirm_selectWithFilterClick);
});
function actionReFormFun(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}

function confirm_selectWithFilterClick() {
    $('#serverlog_table').bootstrapTable('refresh');
}
function queryserverlog() {
	$('#serverlog_table').bootstrapTable ({
    	height:720,
//    	search:true,
//    	searchAlign:'left',
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
    	showExport:true,
//    	exportTypes:['excel'],
    	exportDataType:'all',
    	exportOptions:{  
//    		ignoreColumn: [0,1],  //忽略某一列的索引  
            fileName: 'serverlog',  //文件名称设置  
            worksheetName: 'serverlog',  //表格工作区名称  
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
                username: $('#selectUsername').val(),
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'serverlog-info',
    });
}

function queryuserList() {
    $.ajax({
        type: "get",
        url: "user-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectUsername').append("<option value='" + data[i].username + "'>" + data[i].username + "</option>");
            }
            $('#selectUsername').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

