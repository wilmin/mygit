$(document).ready(function () {
	statsHourly();
	statsDaily();
	statsMonthly();
});

/*每小时在注册*/
function statsHourly(){
	var hourlyTime = 60*60;
	/*每小时在注册*/
	 //异步请求[在线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOnline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times: hourlyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
              //保留两位小数点
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
              //转换成float类型累加  
                var j = parseFloat([d.proportion]);
                count += j;
            });
            
            //设置数据
            var databb = browsers1;
            var datatt = browsers2;
            var y1 = parseFloat(count.toFixed(2));
            getHourlyOnline(databb,datatt,y1);
        },
        error:function(e){
            alert("Request exception!");
        }
    });

}

/*每天在注册*/
function statsDaily(){
	var dailyTime = 60*60*24;
    /*每天在注册*/
    //异步请求[在线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOnline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times:dailyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
                
                var j = parseFloat([d.proportion]);
                count += j;
            });
            //设置数据
            var databb = browsers1;
            var datatt = browsers2;
            var y1 = parseFloat(count.toFixed(2));
            getDailyOffline(databb,datatt,y1);
        },
        error:function(e){
            alert("Request exception!");
        }
    });

}



/*每月在注册*/
function statsMonthly(){
	var dailyTime = 60*60*24*30;
    /*每月在注册*/
    //异步请求[在线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOnline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times:dailyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
                
                var j = parseFloat([d.proportion]);
                count += j;
            });
            //设置数据
            var databb = browsers1;
            var datatt = browsers2;
            var y1 = parseFloat(count.toFixed(2));
            getMonthlyOffline(databb,datatt,y1);
        },
        error:function(e){
            alert("Request exception!");
        }
    });

}

//离线数据提取
function getHourlyOnline(databb,datatt,y1){
	var hourlyTime = 60*60;
	//异步请求[离线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOffline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times:hourlyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
                
                var j = parseFloat([d.proportion]);
                count += j;
            });
            //设置数据
            var databb2=browsers1;
            var datatt2=browsers2;
            var y2 = parseFloat(count.toFixed(2));
          
            getdata(databb,datatt,y1,databb2,datatt2,y2);
        },
        error:function(e){
            alert("Request exception!");
        }
    });
}

//离线数据提取
function getMonthlyOffline(databb,datatt,y1){
	var dailyTime = 60*60*24*30;
	 //异步请求[离线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOffline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times:dailyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
                
                var j = parseFloat([d.proportion]);
                count += j;
            });
            //设置数据
            var databb2=browsers1;
            var datatt2=browsers2;
            var y2 = parseFloat(count.toFixed(2));
            getdata3(databb,datatt,y1,databb2,datatt2,y2);
        },
        error:function(e){
            alert("Request exception!");
        }
    });
}

//离线数据提取
function getDailyOffline(databb,datatt,y1){
	var dailyTime = 60*60*24;
	 //异步请求[离线]数据
    $.ajax({
        type:"post",
        url: "statsHourlyOffline",//提供数据的Servlet
        contentType : 'application/json',
        data: JSON.stringify({times:dailyTime}),
        success:function(data){
        	data = $.parseJSON(data);
        	
        	var count = 0;
            //定义一个数组
            browsers1 = [],
            browsers2 = [],
            //迭代，把异步获取的数据放到数组中
            $.each(data,function(i,d){
                browsers1.push([d.romversion]);
                var num = parseFloat([d.proportion]);
                var bs = num.toFixed(2);
                browsers2.push(parseFloat(bs));
                
                var j = parseFloat([d.proportion]);
                count += j;
            });
            //设置数据
            var databb2=browsers1;
            var datatt2=browsers2;
            var y2 = parseFloat(count.toFixed(2));
            
            getdata2(databb,datatt,y1,databb2,datatt2,y2);
        },
        error:function(e){
            alert("Request exception!");
        }
    });
}

//加载图形
function getdata(databb,datatt,y1,databb2,datatt2,y2){
	var colors = Highcharts.getOptions().colors,
    categories = ['register', 'unregistered'],
    data = [{
        y: y1,
        color: colors[0],
        drilldown: {
            name: 'register version',
            categories: databb,
            data: datatt,
            color: colors[0]
        }
    }, {
        y: y2,
        color: colors[1],
        drilldown: {
            name: 'unregistered version',
            categories: databb2,
            data: datatt2,
            color: colors[1]
        }
    }],
    browserData = [],
    versionsData = [],
    i,
    j,
    dataLen = data.length,
    drillDataLen,
    brightness;
	
// 构建数据数组
for (i = 0; i < dataLen; i += 1) {
    // 添加浏览器数据
    browserData.push({
        name: categories[i],
        y: data[i].y,
        color: data[i].color
    });
    // 添加版本数据
    drillDataLen = data[i].drilldown.data.length;
    for (j = 0; j < drillDataLen; j += 1) {
        brightness = 0.2 - (j / drillDataLen) / 5;
        versionsData.push({
            name: data[i].drilldown.categories[j],
            y: data[i].drilldown.data[j],
            color: Highcharts.Color(data[i].color).brighten(brightness).get()
        });
    }
}
// 创建每时的图表
$('#container').highcharts({
    chart: {
        type: 'pie'
    },
    title: {
        text: 'An hourly register/unregistered boxs'
    },
    subtitle: {
        text: 'The inner loop is register / unregistered, and the outer ring is a specific version.'
    },
    yAxis: {
        title: {
            text: 'Total percentage market share'
        }
    },
    plotOptions: {
        pie: {
            shadow: false,
            center: ['50%', '50%']
        }
    },
    tooltip: {
        valueSuffix: '%'
    },
    series: [{
        name: 'Boxs',
        data: browserData,
        size: '60%',
        dataLabels: {
            formatter: function () {
                return this.y > 5 ? this.point.name : null;
            },
            color: 'white',
            distance: -30
        }
    }, {
        name: 'Occupancy',
        data: versionsData,
        size: '80%',
        innerSize: '60%',
        dataLabels: {
            formatter: function () {
                // 大于1则显示
                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%'  : null;
            }
        }
    }]
});

}


//加载图形
function getdata2(databb,datatt,y1,databb2,datatt2,y2){
	var colors = Highcharts.getOptions().colors,
    categories = ['register', 'unregistered'],
    data = [{
        y: y1,
        color: colors[0],
        drilldown: {
            name: 'register version',
            categories: databb,
            data: datatt,
            color: colors[0]
        }
    }, {
        y: y2,
        color: colors[1],
        drilldown: {
            name: 'unregistered version',
            categories: databb2,
            data: datatt2,
            color: colors[1]
        }
    }],
    browserData = [],
    versionsData = [],
    i,
    j,
    dataLen = data.length,
    drillDataLen,
    brightness;
	
// 构建数据数组
for (i = 0; i < dataLen; i += 1) {
    // 添加浏览器数据
    browserData.push({
        name: categories[i],
        y: data[i].y,
        color: data[i].color
    });
    // 添加版本数据
    drillDataLen = data[i].drilldown.data.length;
    for (j = 0; j < drillDataLen; j += 1) {
        brightness = 0.2 - (j / drillDataLen) / 5;
        versionsData.push({
            name: data[i].drilldown.categories[j],
            y: data[i].drilldown.data[j],
            color: Highcharts.Color(data[i].color).brighten(brightness).get()
        });
    }
}
// 创建每天的图表
$('#container2').highcharts({
    chart: {
        type: 'pie'
    },
    title: {
        text: 'An daily register/unregistered boxs'
    },
    subtitle: {
        text: 'The inner loop is register / unregistered, and the outer ring is a specific version.'
    },
    yAxis: {
        title: {
            text: 'Total percentage market share'
        }
    },
    plotOptions: {
        pie: {
            shadow: false,
            center: ['50%', '50%']
        }
    },
    tooltip: {
        valueSuffix: '%'
    },
    series: [{
        name: 'Boxs',
        data: browserData,
        size: '60%',
        dataLabels: {
            formatter: function () {
                return this.y > 5 ? this.point.name : null;
            },
            color: 'white',
            distance: -30
        }
    }, {
        name: 'Occupancy',
        data: versionsData,
        size: '80%',
        innerSize: '60%',
        dataLabels: {
            formatter: function () {
                // 大于1则显示
                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%'  : null;
            }
        }
    }]
});

}


//加载图形
function getdata3(databb,datatt,y1,databb2,datatt2,y2){
	var colors = Highcharts.getOptions().colors,
    categories = ['register', 'unregistered'],
    data = [{
        y: y1,
        color: colors[0],
        drilldown: {
            name: 'register version',
            categories: databb,
            data: datatt,
            color: colors[0]
        }
    }, {
        y: y2,
        color: colors[1],
        drilldown: {
            name: 'unregistered version',
            categories: databb2,
            data: datatt2,
            color: colors[1]
        }
    }],
    browserData = [],
    versionsData = [],
    i,
    j,
    dataLen = data.length,
    drillDataLen,
    brightness;
	
// 构建数据数组
for (i = 0; i < dataLen; i += 1) {
    // 添加浏览器数据
    browserData.push({
        name: categories[i],
        y: data[i].y,
        color: data[i].color
    });
    // 添加版本数据
    drillDataLen = data[i].drilldown.data.length;
    for (j = 0; j < drillDataLen; j += 1) {
        brightness = 0.2 - (j / drillDataLen) / 5;
        versionsData.push({
            name: data[i].drilldown.categories[j],
            y: data[i].drilldown.data[j],
            color: Highcharts.Color(data[i].color).brighten(brightness).get()
        });
    }
}
// 创建每天的图表
$('#container3').highcharts({
    chart: {
        type: 'pie'
    },
    title: {
        text: 'An monthly register/unregistered boxs'
    },
    subtitle: {
        text: 'The inner loop is register / unregistered, and the outer ring is a specific version.'
    },
    yAxis: {
        title: {
            text: 'Total percentage market share'
        }
    },
    plotOptions: {
        pie: {
            shadow: false,
            center: ['50%', '50%']
        }
    },
    tooltip: {
        valueSuffix: '%'
    },
    series: [{
        name: 'Boxs',
        data: browserData,
        size: '60%',
        dataLabels: {
            formatter: function () {
                return this.y > 5 ? this.point.name : null;
            },
            color: 'white',
            distance: -30
        }
    }, {
        name: 'Occupancy',
        data: versionsData,
        size: '80%',
        innerSize: '60%',
        dataLabels: {
            formatter: function () {
                // 大于1则显示
                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%'  : null;
            }
        }
    }]
});

}


