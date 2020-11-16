function mainLyric(lyric) {
	//格式化歌词
	var lyrics = formatLyric(lyric);

	//创建歌词
	createLyric(lyrics);

	$("#lrcBox").find(".lrc").eq(0).addClass("current");
	
	
	//计算滚动距离
	var minHeight=$("#lrcContainer").height()/2;
	var scrollTop=0;
	$("#lrcBox").find(".lrc").each(function(i,p){
		scrollTop=p.offsetTop<=minHeight?0:p.offsetTop-minHeight;
		p.dataset.scrollTop=scrollTop;
	});
	
	$("#audio").on("timeupdate",function(){
		var this_=this;
		$("#lrcBox").find(".lrc").each(function(i,p){
			if(Math.abs(this_.currentTime-p.dataset.timepoint)<1){
				$(this).addClass("current").siblings().removeClass("current");
				scrollLyric(i,p.dataset.scrollTop);
			}
		})		
	})
		
	var curLine=-1;
	function scrollLyric(i,scrollTop){
		if(curLine!=i){
			$("#lrcContainer").stop(true).animate({"scrollTop":scrollTop},300);
			curLine=i;
		}
	}
	
}

function createLyric(lyrics) {
	$.each(lyrics, function(i, lyric) {
		var p = document.createElement("p");
		p.innerHTML = lyric.lrcstr;
		p.className = "lrc";

		p.dataset.timepoint = lyric.timepoint;
		p.dataset.line = i;
		$("#lrcBox").append(p);
	});
}