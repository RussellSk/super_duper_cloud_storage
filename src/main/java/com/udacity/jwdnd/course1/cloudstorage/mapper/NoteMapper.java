package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    public Note getNote(Integer noteid);

    @Select("SELECT * FROM NOTES")
    public ArrayList<Note> getNotes();

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    public int insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    public int update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    public int delete(Integer noteid);

    @Select("SELECT COUNT(*) FROM NOTES")
    public int countNotes();
}
