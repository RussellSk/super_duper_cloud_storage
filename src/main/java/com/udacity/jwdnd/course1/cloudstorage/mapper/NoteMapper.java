package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid} AND userid = #{userid}")
    public Note getNote(Integer noteid, Integer userid);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    public ArrayList<Note> getNotes(Integer userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    public int insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid} AND userid = #{userid}")
    public int update(Note note, Integer userid);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid} AND userid = #{userid}")
    public int delete(Integer noteid, Integer userid);

    @Select("SELECT COUNT(*) FROM NOTES WHERE userid = #{userid}")
    public int countNotes(Integer userid);
}
