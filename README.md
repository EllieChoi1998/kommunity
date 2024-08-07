# Kommunity : KOSA MSA5기를 위한 커뮤니티

## Copyrights on Team.Kom (팀 닷 콤)

### 멤버 : 김 진석(팀장), 최 혜령(팀원), 장 원석(팀원)

---

## 주의사항

### 저희는 완전 폐쇄형 커뮤니티 애플리케이션 입니다.

아직 베포 전의 상태이므로, 개발자 계정으로 등록되지 않은 사용자는 회원가입이 불가능 하여 채팅 기능을 이용하시지 못하십니다. (두개이상의 계정 접속이 되지 않습니다.)

- 두개 이상의 계정으로 접속하여 애플리케이션을 이용하고 싶으신 분들은,
- 애플리케이션 실행방법 - 4단계의 admin 계정 생성 코드를 직접 이용하여 수동으로 데이터베이스에 계정 생성을 하시거나, 관리자 페이지로 이동 후 멤버 추가를 해주세요 !

## 애플리케이션 실행 방법 :

1. Oracle DBMS 21c를 설치해 두세요
    1. sqlplus 혹은 sqldeveloper를 통해 아래의 내용을 입력하여 데이터베이스 계정을 생성해 두세요
    
    ```sql
    drop user kom cascade;
    create user kom
    identified by kom
    default tablespace users
    temporary tablespace temp;
      
    alter user kom
    quota unlimited on users;
      
    grant connect, resource
    to kom;
    ```
    
2. 애플리케이션을 실행하여 테이블들을 생성합니다.
3. sqlplus 혹은 sqldeveloper를 통해 kom 계정으로 접속해 주세요.
    1. 아래의 프로시저 코드를 생성해 주세요
        
        ```sql
        CREATE OR REPLACE PROCEDURE cleanup_chats AS
        BEGIN
            -- 오래된 레코드를 삭제하기 위한 서브쿼리
            DELETE FROM chat
            WHERE chat_id IN (
                SELECT chat_id
                FROM (
                    SELECT chat_id,
                           ROW_NUMBER() OVER (ORDER BY chat_datetime ASC) AS rn
                    FROM chat
                ) subquery
                WHERE rn <= (SELECT COUNT(*) FROM chat) - 100
            );
        END;
        /
        ```
        
4. 애플리케이션 소스코드의 아래 경로를 통해, 테스트 코드를 실행을 통한 admin 계정을 생성해 주세요.
    
    ```sql
    src/test/java/com/kosa/kmt/nonController/member/AdminCreateNewUserTest.java
    ```
