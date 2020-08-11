package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileid} AND userid = #{userid}")
    public File findById(Integer fileid, Integer userid);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    public File findByFilename(String filename, Integer userid);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    public ArrayList<File> files(Integer userid);

    @Delete("DELETE FROM FILES WHERE fileId = #{id} and userid = #{userid}")
    public int delete(Integer id, Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public int create(File file);

    @Select("SELECT COUNT(*) FROM FILES WHERE userid = #{userid}")
    public int countFiles(Integer userid);
}
