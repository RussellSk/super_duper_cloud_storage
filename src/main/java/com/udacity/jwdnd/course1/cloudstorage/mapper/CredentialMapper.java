package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id} AND userid = #{userid}")
    public Credential findById(Integer id, Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    public ArrayList<Credential> credentials(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, `key`, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    public int insert(Credential credential);

    @Select("SELECT COUNT(*) FROM CREDENTIALS WHERE userid = #{userid}")
    public int countCredential(Integer userid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} WHERE credentialid = #{credentialid} AND userid = #{userid}")
    public int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id} AND userid = #{userid}")
    public int delete(Integer id, Integer userid);
}
