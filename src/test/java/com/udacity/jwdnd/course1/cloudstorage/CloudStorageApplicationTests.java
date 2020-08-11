package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	private final String username = "usernametest";
	private final String password = "megapasswordtest";

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait webDriverWait;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().timeout(10).setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.webDriverWait = new WebDriverWait(driver, 30);

		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Russell", "Skalden", username, password);

		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
			driver = null;
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUserSignupLogin() {
		Assertions.assertEquals("Dashboard", driver.getTitle());
	}

	@Test
	public void testDashboardPage() {
		driver.get("http://localhost:" + this.port + "/dashboard");
		Assertions.assertEquals("Dashboard", driver.getTitle());
	}

	@Test
	public void testFilesPage() {
		driver.get("http://localhost:" + this.port + "/file");
		Assertions.assertEquals("Files", driver.getTitle());
	}

	@Test
	public void testCredentialsPage() {
		driver.get("http://localhost:" + this.port + "/credential");
		Assertions.assertEquals("Credentials", driver.getTitle());
	}

	@Test
	public void testNotesPage() {
		driver.get("http://localhost:" + this.port + "/note");
		Assertions.assertEquals("Notes", driver.getTitle());
	}

	@Test
	public void testUserPage() {
		driver.get("http://localhost:" + this.port + "/user");
		Assertions.assertEquals("Users", driver.getTitle());
	}

	@Test
	@Order(1)
	public void testCreateNote() {
		String noteTitle = "Cloud Note Test";
		String noteDescription = "Cloud Note Description Test";

		driver.get("http://localhost:" + this.port + "/note");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		NotesPage notesPage = new NotesPage(driver);
		notesPage.createNote(noteTitle, noteDescription);

		Note note = notesPage.getFirstNote();
		Assertions.assertEquals(noteTitle, note.getNotetitle());
		Assertions.assertEquals(noteDescription, note.getNotedescription());
	}

	@Test
	@Order(2)
	public void testEditNote() {
		String noteTitle = "Test Edit Title";
		String noteDescription = "Test Edit description";

		driver.get("http://localhost:" + this.port + "/note");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.className("noteEditButton"))).click();

		WebElement elementTitle = driver.findElement(By.id("notetitle"));
		elementTitle.clear();
		WebElement elementDescription = driver.findElement(By.id("notedescription"));
		elementDescription.clear();

		webDriverWait.until(ExpectedConditions.elementToBeClickable(elementTitle)).sendKeys(noteTitle);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(elementDescription)).sendKeys(noteDescription);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteEditModalButton"))).click();

		NotesPage notesPage = new NotesPage(driver);
		Note note = notesPage.getFirstNote();

		Assertions.assertEquals(noteTitle, note.getNotetitle());
		Assertions.assertEquals(noteDescription, note.getNotedescription());
	}

	@Test
	@Order(3)
	public void testDeleteNote() {
		driver.get("http://localhost:" + this.port + "/note");

		WebElement elementDeleteButton = driver.findElement(By.className("noteDeleteButton"));
		elementDeleteButton.click();

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			NotesPage notesPage = new NotesPage(driver);
			Note note = notesPage.getFirstNote();
		});

	}
}
