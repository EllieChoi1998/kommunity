package com.kosa.kmt.member;

import java.time.LocalDateTime;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    return 1 = Login Success
    return 0 = update loginTime failed
    reuturn -1 = password unmatched
    return -2 = email not found
     */
    @Override
    public Integer login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()){
            Member currentMember = member.get();
            String userPassword = currentMember.getPassword();
            if (password.equals(userPassword)){
                Member updatedmember = memberRepository.updateLoginTime(currentMember, LocalDateTime.now());
                if (updatedmember != null){
                    return 1;
                }
                return 0;
            }
            return -1;
        }
        return -2;
    }

    @Override
    public Boolean logout(Member member) {
        Member updatedmember = memberRepository.updateLogoutTime(member, LocalDateTime.now());
        if (updatedmember != null){
            return true;
        }
        return false;
    }

    /*
    Signup must be called at the very last stage in the Controller.
    For signup, there's three stages:
        1. call findSameEmail() -- should return true
        2. call findSameName() -- should return true
        3. save() -- return new memberId
     */

    @Override
    public Integer save(Member member) {
        memberRepository.save(member);
        return member.getMemberId();
    }

    /*
    로그인을 위한 이메일 및 이름 찾기 입니다.
    findSameName, findSameEmail 둘다 같은 로직으로,
    각 값을 가진 멤버 데이터를 찾으면 returns memberId.
    (폐쇄형 애플리케이션 특성 상, 멤버들의 이메일과 이름은 이미 데이터에 저장되어 있습니다).
     */
    @Override
    public Integer findSameName(String name) {
        Integer foundId = memberRepository.findByName(name).get().getMemberId();
        if(foundId != null) {
            return foundId;
        } else {
            return -1;
        }
    }
    @Override
    public Integer findSameEmail(String email) {
        Integer foundId = memberRepository.findByEmail(email).get().getMemberId();
        if(foundId != null) {
            return foundId;
        } else {
            return -1;
        }
    }


    @Override
    public Boolean ExtendLogin(Member member) {
        Member updatedMember = memberRepository.updateExtendLogin(member, "T");
        if(updatedMember != null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean CancelExtendLogin(Member member) {
        Member updatedMember = memberRepository.updateExtendLogin(member, "F");
        if(updatedMember != null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateNickname(Member member, String nickname) {
        Member updatedMember = memberRepository.update_nickname(member, nickname);
        if(updatedMember != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updatePassword(Member member, String password) {
        Member updatedMember = memberRepository.update_pssword(member, password);
        if(updatedMember != null) {
            return true;
        }
        return false;
    }


}
