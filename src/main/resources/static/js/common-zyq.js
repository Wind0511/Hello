 /* 时间格式转换*/
var formatTime=function (seconds) {
	var h=0,
		i=0,
		s=Math.floor(seconds);
		h=Math.floor(s/3600);
		i=Math.floor((s%3600)/60);
		s=s%3600%60;

	return {
		H:h=h<10?"0"+h:h,
		I:i=i<10?"0"+i:i,
		S:s=s<10?"0"+s:s
	};
};

 // 日期格式转换
var formatDate=function (timestamp) {
	var now=new Date(timestamp);
	var y=now.getFullYear(),
		m=+1+now.getMonth(),
		d=now.getDate();

	return {
		Y:y,
		M:m=m<10?"0"+m:m,
		D:d=d<10?"0"+d:d
	};
}

//  请求API (PHP) GET
/*function requestAPI(req) {
	$.ajax({
		url:req.url,
		type:"GET",
		data:req.data,
		success:function (res) {
			req.callback(JSON.parse(res)); // str->json
		},
		error:function (err) {
			console.warn(err.status);
		}
	});
}*/

// create jsonp
function requestAPI(reqdata) {
	$.ajax({
		url:reqdata.url,
		data:reqdata.data,
		dataType:"jsonp",
		jsonp:"callback",
		success:function (res) {				
			reqdata.callback(res);
		}/*,
		error:function (err) {
			console.warn(err.status);
		}*/
	});
}

function formatLyricTime(str) {
	var arr = str.split(":");
	var second = 0;
	if(arr.length == 3) {
		second = -(-arr[0] * 3600 - arr[1] * 60 - arr[2]);
	} else {
		second = -(-arr[0] * 60 - arr[1]);
	}
	return second.toFixed(3);
}
//歌单格式
function formatLyric(str) {
	var arr = [],
		brr = [],
		crr = [],
		data = {};


	arr = str.split("\n");
	arr.splice(-1, 1);
	for(var i = 0; i < arr.length; i++) {
		brr = arr[i].split("]");
		if(!!/^(\d+:){1,2}\d+\.?\d+$/g.test(brr[0].substring(1))) {
			data = {
				"timepoint": formatLyricTime(brr[0].substring(1)),
				"lrcstr": brr[1] || "<br />"
			};
			crr.push(data);
		} else {
		}
	}
	return crr;
}
//全局地址
window.requestURL="http://www.igeekhome.com/mplayer/api.php";

$(function () {
	window.formatTime=formatTime;
	window.formatDate=formatDate;
	window.requestAPI=requestAPI;
	window.formatLyric=formatLyric;
});
