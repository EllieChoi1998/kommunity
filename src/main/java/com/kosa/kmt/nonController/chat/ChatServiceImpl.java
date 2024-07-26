package com.kosa.kmt.nonController.chat;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;


    public ChatServiceImpl(ChatRepository chatRepository, MemberRepository memberRepository) {
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Long saveChat(Chat chat, Integer memberId) throws Exception {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            chat.setMember(optionalMember.get());
            Chat savedChat = chatRepository.save(chat);
            if(findAllChats().size()>100) {
                chatRepository.executeCleanup();
            }
            return savedChat.getChatId();
        } else {
            return (long) -1;
        }
    }

    @Override
    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }


}
