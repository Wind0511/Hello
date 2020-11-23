$(document).ready(function () {
    sessionStorage.clear();
    var data = sessionStorage.getItem('slist');
    // $.ajax({
    //     type: 'get',
    //     dataType: 'json',
    //     url: '/album?id='+request("id"),
    //
    //     success:
    //         function (data) {
    //             console.log(data);
    //
    //
    //
    //             initalbum(data);
    //             refeshDOM1();
    //
    //             var songTotals = data.length;
    //             localStorage.setItem("songTotals", songTotals);
    //
    //         },
    //     error: function () {
    //         alert('错了1');
    //     }
    // });

    function refeshDOM2() {

        var firstTR = $("#infoList_playlist").find("tr").get(0);
        $("#audio").prop("src", firstTR.dataset.mp3url);

        $(firstTR).find("td.index").html('<i class="fa fa-volume-up" aria-hidden="true"></i>').addClass("active");

        //初始化小窗口信息
        $("#albumPic").prop("src", firstTR.dataset.albumPic);
        $("#muiscName").html(firstTR.dataset.name);
        $("#artistName").html(firstTR.dataset.artistName);

        $("#infoPoster").prop("src", firstTR.dataset.albumPic);

        //初始化详情页信息
        $("#bgDisc").css("background-image", "url('" + firstTR.dataset.albumPic + "')");
        $("#bgBlur").css("background-image", "url('" + firstTR.dataset.albumPic + "')");

        $("#songname").html(firstTR.dataset.name);
        $("#albumname").html(firstTR.dataset.album);
        $("#singersname").html(firstTR.dataset.artistName);

        $.ajax({
            type: 'get',
            dataType: 'json',
            url: '/lrc?id=' + firstTR.dataset.id,
            success:
                function (data) {

                    mainLyric(data.lrc.lyric);
                },
            error: function () {
                alert('错了2');
            }
        });

    }
    var a;
    $.ajax({
        type: 'get',
        dataType: 'text',
        url: '/sp?id=' + data.albumId,
        success:
            function (data) {
                //alert(data)
                a=data;
            },
        error: function () {
            return "error";
        }
    });




    function initalbum(data) {

        var num, name,
            artistName, album, tr;
        $.each(data, function (index, track) {
            num = (index + 1 < 100) ? "0" + (index + 1) : index + 1;
            name = track.songName;
            artistName = track.singerName;
            album = track.albumName;
            albumId = track.albumId;

            tr = document.createElement("tr");
            tr.dataset.id = track.songId;
            tr.dataset.index = index;
            tr.dataset.mp3url = "http://music.163.com/song/media/outer/url?id=" + track.songId + ".mp3"
            //tr.dataset.albumId = track.albumId;
            tr.dataset.name = name;
            tr.dataset.artistName = artistName;
            tr.dataset.album = album;


            tr.dataset.albumPic = a;
            tr.innerHTML = '<td class="index" data-num="' + num + '">' + num + '</td>' +
                '<td><i class="fa fa-heart-o" aria-hidden="true"></i>&nbsp;' +
                '<i class="fa fa-download" aria-hidden="true"></i></td>' +
                '<td>' + name + '</td>' +
                '<td>' + artistName + '</td>' +
                '<td>' + album + '</td>' +
                '<td>' + 'xx' + '</td>';

            $("#infoList_playlist").append(tr);
        });

    }

    $("#poster").on("click", function () {
        $("#pageSongDetail").css({
            "top": "60px",
            "right": "0",
            "opacity": "1"
        });
    });

    $("#btnCompressPlayBox").on("click", function () {
        $("#pageSongDetail").css({
            "top": "100%",
            "right": "100%",
            "opacity": "0"
        });
    })
});
function refeshDOM2(i) {

    var firstTR = $("#infoList_playlist").find("tr").get(i);
    $("#audio").prop("src", firstTR.dataset.mp3url);

    $(firstTR).find("td.index").html('<i class="fa fa-volume-up" aria-hidden="true"></i>').addClass("active");

    //初始化小窗口信息
    $("#albumPic").prop("src", firstTR.dataset.albumPic);
    $("#muiscName").html(firstTR.dataset.name);
    $("#artistName").html(firstTR.dataset.artistName);

    $("#infoPoster").prop("src", firstTR.dataset.albumPic);

    //初始化详情页信息
    $("#bgDisc").css("background-image", "url('" + firstTR.dataset.albumPic + "')");
    $("#bgBlur").css("background-image", "url('" + firstTR.dataset.albumPic + "')");

    $("#songname").html(firstTR.dataset.name);
    $("#albumname").html(firstTR.dataset.album);
    $("#singersname").html(firstTR.dataset.artistName);

    $.ajax({
        type: 'get',
        dataType: 'json',
        url: '/lrc?id=' + firstTR.dataset.id,
        success:
            function (data) {

                mainLyric(data.lrc.lyric);
            },
        error: function () {
            alert('错了2');
        }
    });

}