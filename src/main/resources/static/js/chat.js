document.addEventListener("DOMContentLoaded", function() {
    var stompClient = null;
    var isConnected = false; // WebSocket 연결 상태 추적
    var messageIds = new Set(); // 메시지 ID 집합
    var currentUserId = null;
    function connect() {
        if (isConnected) return; // 이미 연결된 경우 반환

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);

            stompClient.subscribe('/topic/public', function(message) {
                var msg = JSON.parse(message.body);
                if (!messageIds.has(msg.chatDateTime)) {
                    showMessage(msg);
                    messageIds.add(msg.chatDateTime); // 메시지 ID 저장
                }
            });

            // Load existing messages
            $.get('/kommunity/chat/getAllChats', function(response) {
                if(currentUserId == null){
                    currentUserId = response.currentUserId;
                    console.log('current user: ', currentUserId);
                }
                updateChatMessages(response);
            });

            isConnected = true; // WebSocket 연결 상태 업데이트
        });
    }

    function sendMessage() {
        var messageContent = $('#chat-input').val().trim();
        if (messageContent && stompClient) {
            var chatMessage = {
                chatContent: messageContent
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            $('#chat-input').val('');
        }
    }

    function showMessage(message) {
        var chatMessagesDiv = $('#chat-messages');
        var chatElement = $('<div>').addClass('chat-message');
        var messageText = message.chatWriterName + ' (' + new Date(message.chatDateTime).toLocaleString() + '): ' + message.chatContent;

        console.log('whoami : ', message.isCurrentUser)
        if (message.chatWriterId == currentUserId) {
            chatElement.addClass('current-user'); // 현재 사용자의 메시지
        } else {
            chatElement.addClass('other-user'); // 다른 사용자의 메시지
        }

        chatElement.text(messageText);
        chatMessagesDiv.append(chatElement);

        scrollToBottom(); // 새 메시지가 추가되면 맨 아래로 스크롤
    }

    function updateChatMessages(data) {
        var chatMessagesDiv = $('#chat-messages');
        chatMessagesDiv.empty();
        if (data && data.chatContents) {
            data.chatContents.forEach(function(chat) {
                if (!messageIds.has(chat.chatDateTime)) {
                    showMessage(chat);
                    messageIds.add(chat.chatDateTime); // 메시지 ID 저장
                }
            });
        }
    }

    $('#chat-send').on('click', function() {
        sendMessage();
    });

    $('#chat-input').on('keypress', function(e) {
        if (e.which === 13) {
            sendMessage();
        }
    });

    // 채팅창 열리고 닫히고 + 맨 아래 채팅으로 이동
    let isExpanded = localStorage.getItem('chatExpanded') === 'true';

    // 채팅창 상태를 적용
    if (isExpanded) {
        $('#chat-container').addClass('expanded');
        scrollToBottom(); // 채팅창이 확장된 상태라면 맨 아래로 스크롤
    } else {
        $('#chat-container').removeClass('expanded');
    }

    // 채팅창 높이 조절
    $('.chat-header').on('click', function() {
        if (!isExpanded) {
            $('#chat-container').addClass('expanded');
            isExpanded = true; // 상태 업데이트
            $('#chat-container').on('transitionend', function() {
                scrollToBottom(); // 애니메이션이 끝난 후 맨 아래로 스크롤
                $('#chat-container').off('transitionend');
            });
        } else {
            $('#chat-container').removeClass('expanded');
            isExpanded = false; // 상태 업데이트
        }
        localStorage.setItem('chatExpanded', isExpanded); // 로컬 스토리지에 상태 저장
    });

    // 채팅창을 맨 아래로 스크롤하는 함수
    function scrollToBottom() {
        let chatMessages = $('.chat-messages');
        chatMessages.scrollTop(chatMessages.prop("scrollHeight"));
    }

    connect();
});
