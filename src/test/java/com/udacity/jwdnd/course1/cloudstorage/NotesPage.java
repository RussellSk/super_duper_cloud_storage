package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotesPage {
    @FindBy(css = "#createNotetitle")
    private WebElement notetitle;

    @FindBy(css = "#createNotedescription")
    private WebElement notedescription;

    @FindBy(css = "#create-note-btn")
    private WebElement createNoteButton;

    @FindBy(css = "#createModalButton")
    private WebElement createModalButton;

    @FindBy(className = "noteTitle")
    private WebElement resultNoteTitle;

    @FindBy(className = "noteDescription")
    private WebElement resultNoteDescription;

    public NotesPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void createNote(String title, String description) {
        this.createModalButton.click();
        this.notetitle.sendKeys(title);
        this.notedescription.sendKeys(description);
        this.createNoteButton.click();
    }

    public Note getFirstNote() {
        Note result = new Note();
        result.setNotetitle(resultNoteTitle.getText());
        result.setNotedescription(resultNoteDescription.getText());
        return result;
    }
}
