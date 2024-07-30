$(document).ready(function() {

    var header = $("meta[name='_csrf_header']").attr('content');
    var token = $("meta[name='_csrf']").attr('content');

    // function getCsrfToken() {
    //     var name = 'X-CSRF-TOKEN=';
    //     var decodedCookie = decodeURIComponent(document.cookie);
    //     var ca = decodedCookie.split(';');
    //     for (var i = 0; i < ca.length; i++) {
    //         var c = ca[i];
    //         while (c.charAt(0) == ' ') {
    //             c = c.substring(1);
    //         }
    //         if (c.indexOf(name) == 0) {
    //             return c.substring(name.length, c.length);
    //         }
    //     }
    //     return "";
    // }
    //
    // $.ajaxSetup({
    //     headers: {
    //         'X-CSRF-TOKEN': getCsrfToken()
    //     }
    // });

    $('.chat-header').on('click', function() {
        $('#chat-container').toggleClass('expanded');

        // 채팅 데이터를 비동기적으로 가져오기
        $.ajax({
            url: '/kommunity/chat/getAllChats',
            method: 'GET',
            success: function(response) {
                // AJAX 호출이 성공하면 채팅 데이터를 업데이트합니다.
                updateChatMessages(response);
            },
            error: function(xhr, status, error) {
                console.error('채팅 데이터를 가져오는 데 실패했습니다:', error);
            }
        });
    });

    // 채팅 메시지를 업데이트하는 함수
    function updateChatMessages(data) {
        var chatMessagesDiv = $('#chat-messages');
        chatMessagesDiv.empty(); // 기존 메시지 삭제

        if (data && data.chatContents) {
            data.chatContents.forEach(function(chat) {
                var chatElement = $('<div>').addClass('chat-message');

                // 추가: 유저 이름과 시각을 포함한 텍스트 구성
                var messageText = chat.chatWriterName + ' (' + new Date(chat.chatDateTime).toLocaleString() + '): ' + chat.chatContent;
                chatElement.text(messageText);

                // 콘솔 로그로 클래스 확인
                console.log(chat.chatContent, chat.isCurrentUser ? 'current-user' : 'other-user');

                if (chat.isCurrentUser) {
                    chatElement.addClass('current-user'); // 현재 사용자의 메시지
                } else {
                    chatElement.addClass('other-user'); // 다른 사용자의 메시지
                }
                chatMessagesDiv.append(chatElement);
            });
        }
    }

    $('#chat-send').on('click', function() {
        var messageInput = $('#chat-input');
        var message = messageInput.val();

        if (message) {
            $.ajax({
                url: '/kommunity/chat/sendChat',

                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ content: message }),
                xhrFields: {
                    withCredentials: true
                },
                beforeSend: function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if (response && response.chatContent) {
                        var chatElement = $('<div>').addClass('chat-message');
                        var messageText = response.chatContent.chatWriterName + ' (' + new Date(response.chatContent.chatDateTime).toLocaleString() + '): ' + response.chatContent.chatContent;
                        chatElement.text(messageText);

                        if (response.chatContent.isCurrentUser) {
                            chatElement.addClass('current-user');
                        } else {
                            chatElement.addClass('other-user');
                        }

                        $('#chat-messages').append(chatElement);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('채팅 데이터를 보내는 데 실패했습니다:', error);
                    console.error('응답 상태:', status);
                    console.error('응답 본문:', xhr.responseText);
                    console.error('전체 요청 객체:', xhr);
                }
            });
            messageInput.val('');
        }
    });

});
