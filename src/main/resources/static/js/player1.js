//暂停和播放，上一个和下一个
$(function() {

	var curIndex = 0;
	var isDrag = false;
	var audio = $("#audio").get(0);

	$(".play").click(function() {
		if (audio.paused) {
			audio.play();
			changePlayStyle('play');
		} else {
			audio.pause();
			changePlayStyle('pause');
		}
	})

	$(".forward").click(function() {
		var songTotals = localStorage.getItem("songTotals");
		curIndex = (curIndex + 1 >= songTotals) ? 0 : curIndex + 1;
		playMusic(curIndex);
	});

	$(".backward").click(function() {
		var songTotals = localStorage.getItem("songTotals");
		curIndex = (curIndex - 1 < 0) ? songTotals - 1 : curIndex - 1
		playMusic(curIndex);
	});

	$("#infoList_playlist").on("dblclick", "tr", function() {
		curIndex = parseInt(this.dataset.index);
		refeshDOM1(curIndex);
		playMusic(curIndex);
	});
	//音乐播放进度条缓冲
	$("#audio").on("timeupdate", function() {

		if (!isDrag) {
			var currentTime = formatTime(this.currentTime);
			var duration = formatTime(this.duration);

			$("#time_start").html(currentTime.I + ":" + currentTime.S);
			$("#time_end").html(duration.I + ":" + duration.S);

			var prc = (this.currentTime / this.duration * 100).toFixed(2);
			$("#process_current").css("width", prc + "%");
		}

	});
	//音乐播放结束后触发，自动播放
	$("#audio").on("ended", function() {
		var songTotals = localStorage.getItem("songTotals");
		curIndex = curIndex + 1;
		if (curIndex > songTotals) {
			audio.pause();
			changePlayStyle('pause');
		} else {
			playMusic(curIndex);
		}
	});

	$("#circle").on("mousedown", function(event) {
		//console.log("mousedown "+event.clientY);
		//console.log(this.getBoundingClientRect().left);
		var changeVal = 0;
		var play_processRect = $("#play_process").get(0).getBoundingClientRect();

		var disX = event.clientX - this.getBoundingClientRect().left;

		var moveArc = function(event) {
			//console.log("mousemove "+event.clientX);
			var prc = (((event.clientX - disX - play_processRect.left) / play_processRect.width) * 100).toFixed(2);
			prc = prc < 0 ? 0 : (prc > 100 ? 100 : prc);

			$("#process_current").width(prc + "%");

			isDrag = true;
			changeVal = (audio.duration * prc / 100).toFixed(2);
			var changeTime = formatTime(changeVal);
			$("#time_start").html(changeTime.I + ":" + changeTime.S);

		}

		var upArc = function() {
			$(document).off("mousemove", moveArc);
			$(document).off("mouseup", upArc);

			isDrag = false;
			audio.currentTime = changeVal;

		}

		$(document).on("mousemove", moveArc);
		$(document).on("mouseup", upArc);

	});
	$("#volume_circle").on("mousedown", function(event) {
		var vol_processRect = $("#volume_process").get(0).getBoundingClientRect();

		var disX = event.clientX - this.getBoundingClientRect().left;

		var moveArc = function(event) {
			var prc = (((event.clientX - disX - vol_processRect.left) / vol_processRect.width) * 100).toFixed(2);
			prc = prc < 0 ? 0 : (prc > 100 ? 100 : prc);

			$("#volume_current").width(prc + "%");
              audio.volume=prc/100;
		}

		var upArc = function() {
			$(document).off("mousemove", moveArc);
			$(document).off("mouseup", upArc);

			isDrag = false;
			audio.currentTime = changeVal;

		}

		$(document).on("mousemove", moveArc);
		$(document).on("mouseup", upArc);

	});

	$("#audio").on("play", function() {
		$("#bgDisc").css("animation-play-state", "running");
	});


	$("#audio").on("pause", function() {
		$("#bgDisc").css("animation-play-state", "paused");
	});


	setInterval(function() {
		if (audio.readyState == 4) {
			var timeRanges = audio.buffered;

			var lastTime = timeRanges.end(timeRanges.length - 1);
			var duration = audio.duration;

			var percent = (lastTime / duration * 100).toFixed(2);
			$("#process_cache").css("width", percent + "%");
		}
	}, 1000);





	function playMusic(index) {
		audio.pause();
		changePlayStyle('pause');

		trs = $("#infoList_playlist").find("tr");
		curTR = trs.get(index);

		$(audio).prop("src", curTR.dataset.mp3url);

		audio.play();
		changePlayStyle('play');


		$(trs).find("td.index").each(function(i, td) {
			$(td).html(td.dataset.num).removeClass("active");
		})

		$(curTR).find("td.index").html('<i class="fa fa-volume-up" aria-hidden="true"></i>').addClass("active");
	}


	function changePlayStyle(type) {
		var pauseHtml = '<i class="fa fa-pause" aria-hidden="true"></i>';
		var playHtml = '<i class="fa fa-play" aria-hidden="true"></i>';
		$(".play").html(type == 'play' ? pauseHtml : playHtml);
	}

	//  function musicVoiceSeekTo(value) {
	//     if(isNaN(value)) return;
	//     if(value <0 || value > 1) return;
	//     this.audio.volume = value;
	// }

})

// (function(window) {
// 	function Player($audio) {
// 		return new Player.prototype.init($audio);
// 	}
// 	Player.prototype = {
// 	    constructor: Player,
// 		musicVoiceSeekTo: function (value) {
// 		    if(isNaN(value)) return;
// 		    if(value <0 || value > 1) return;
// 		    this.audio.volume = value;
// 		}
// 	}
// 	Player.prototype.init.prototype = Player.prototype;
// 	window.Player = Player;
// })(window);
