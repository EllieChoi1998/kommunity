$(document).ready(function() {
    // 전송 버튼 누르는 이벤트
    $("#chat-send").on("click", function (e) {
        sendMessage();
        $('#chat-input').val('');
    });

    var sock = new SockJS('http://localhost:8083/chatting');
    sock.onmessage = onMessage;
    sock.onclose = onClose;
    sock.onopen = onOpen;

    function sendMessage() {
        sock.send($("#chat-input").val());
    }

    // 서버에서 메시지를 받았을 때
    function onMessage(msg) {
        var data = msg.data;
        var sessionId = null; // 데이터를 보낸 사람
        var message = null;

        var arr = data.split(":");

        for (var i = 0; i < arr.length; i++) {
            console.log('arr[' + i + ']: ' + arr[i]);
        }

        var cur_session = '${userid}'; // 현재 세션에 로그인 한 사람
        console.log("cur_session : " + cur_session);

        sessionId = arr[0];
        message = arr[1];

        // 로그인 한 클라이언트와 타 클라이언트를 분류하기 위함
        var str = "<div class='col-6'>";
        if (sessionId == cur_session) {
            str += "<div class='alert alert-secondary'>";
        } else {
            str += "<div class='alert alert-warning'>";
        }
        str += "<b>" + sessionId + " : " + message + "</b>";
        str += "</div></div>";

        $("#chat-messages").append(str);
    }

    // 채팅창에서 나갔을 때
    function onClose(evt) {
        var user = '${pr.username}';
        var str = user + " 님이 퇴장하셨습니다.";
        $("#chat-messages").append(str);
    }

    // 채팅창에 들어왔을 때
    function onOpen(evt) {
        var user = '${pr.username}';
        var str = user + "님이 입장하셨습니다.";
        $("#chat-messages").append(str);
    }
});
