package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileid}")
    public File findById(Integer fileid);

    @Select("SELECT * FROM FILES")
    public ArrayList<File> files();

    @Delete("DELETE FROM FILES WHERE fileId = #{id}")
    public int delete(Integer id);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public int create(File file);

    @Select("SELECT COUNT(*) FROM FILES")
    public int countFiles();
}
