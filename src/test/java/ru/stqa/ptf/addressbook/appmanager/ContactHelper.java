package ru.stqa.ptf.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.ptf.addressbook.model.ContactData;
import ru.stqa.ptf.addressbook.model.Contacts;

import java.util.List;

public class ContactHelper extends HelperBase {
    public ContactHelper(WebDriver driver) {
        super(driver);
    }

    public void fillContactForm(ContactData contactData) {
        type(By.name("firstname"), contactData.getFirstname());
        type(By.name("middlename"), contactData.getMiddlename());
        type(By.name("lastname"), contactData.getLastname());
        type(By.name("email"), contactData.getEmail());
        type(By.name("email2"), contactData.getEmail2());
        type(By.name("email3"), contactData.getEmail3());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        attach(By.name("photo"), contactData.getPhoto());
    }

    public void gotoHomePage() {
        click(By.linkText("home"));
    }

    public void gotoAddNewPage() {
        click(By.linkText("add new"));
    }

    public void submitContact() {
        click(By.name("submit"));
    }

    public void selectContactById(int id) {
        driver.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    public void goToModifyContact(int id) {
        driver.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void deleteSelectedContact() {
        click(By.xpath("/html/body/div[1]/div[4]/form[2]/div[2]/input"));
    }

    public void closeDialogWindow() {
        driver.switchTo().alert().accept();
    }

    public int count() {
        return driver.findElements(By.name("selected[]")).size();
    }

    public void create(ContactData contact) {
        gotoAddNewPage();
        fillContactForm(contact);
        submitContact();
        contactCache = null;
        gotoHomePage();
    }

    public void modify(ContactData contact) {
        gotoHomePage();
        goToModifyContact(contact.getId());
        fillContactForm(contact);
        submitContactModification();
        contactCache = null;
        gotoHomePage();
    }

    public void delete(ContactData contact) {
        gotoHomePage();
        selectContactById(contact.getId());
        deleteSelectedContact();
        closeDialogWindow();
        contactCache = null;
        gotoHomePage();
    }

    private void initContactModificationById(int id) {
        WebElement checkbox = driver.findElement(By.cssSelector(String.format("input[value='%s']", id)));
        WebElement row = checkbox.findElement(By.xpath("./../.."));
        List<WebElement> cells = row.findElements(By.tagName("td"));
        cells.get(7).findElement(By.tagName("a")).click();
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        String firstname = driver.findElement(By.name("firstname")).getAttribute("value");
        String lastname = driver.findElement(By.name("lastname")).getAttribute("value");
        String home = driver.findElement(By.name("home")).getAttribute("value");
        String mobile = driver.findElement(By.name("mobile")).getAttribute("value");
        String work = driver.findElement(By.name("work")).getAttribute("value");
        String email = driver.findElement(By.name("email")).getAttribute("value");
        String email2 = driver.findElement(By.name("email2")).getAttribute("value");
        String email3 = driver.findElement(By.name("email3")).getAttribute("value");
        driver.navigate().back();
        return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
                .withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work)
                .withEmail(email).withEmail2(email2).withEmail3(email3);
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = driver.findElements(By.cssSelector("#maintable [name=entry]"));
        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.cssSelector("input")).getAttribute("value"));
            List<WebElement> tds = element.findElements(By.cssSelector("td"));
            String lastname = tds.get(1).getText();
            String firstname = tds.get(2).getText();
            String allPhones = tds.get(5).getText();
            String allEmail = tds.get(4).getText();
            contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname)
                    .withAllPhones(allPhones).withAllEmail(allEmail));
        }
        return new Contacts(contactCache);
    }

}