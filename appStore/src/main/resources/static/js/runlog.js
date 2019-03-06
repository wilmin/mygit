$(document).ready(function () {
	queryrunlog();
	$('#confirm_selectWithFilter').bind("click", confirm_selectWithFilterClick);
});
function actionReFormFun(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}

function confirm_selectWithFilterClick() {
    $('#runlog_table').bootstrapTable('refresh');
}
function queryrunlog() {
	$('#runlog_table').bootstrapTable ({
    	height:720,
//    	search:true,  //开启查询
//    	searchAlign:'left',
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
    	showExport:true,
//    	exportTypes:['excel'],
    	exportDataType:'all',
    	exportOptions:{  
//    		ignoreColumn: [0,1],  //忽略某一列的索引  
            fileName: 'runlog',  //文件名称设置  
            worksheetName: 'runlog',  //表格工作区名称  
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
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'runlog-info',
    });
}

