package remind.remember.fix.utils;

import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import remind.remember.fix.dao.PersonDAO;
import remind.remember.fix.models.Person;

@Component
public class PersonValidator implements Validator {

    private PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO){
        this.personDAO = personDAO;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(personDAO.show(person.getEmail()).isPresent()){
            errors.rejectValue("email", "", "Dublicate email");
        }
    }
    
}
