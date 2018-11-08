package io.yetanotherwhatever.emailer;

import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class EmailMonkeyTest {

    @Test
    public void emailAll_emailList_eachEmailed()
    {
        ArrayList<Contact> cl = new ArrayList<>();

        cl.add(new Contact("first", "last", "email"));
        cl.add(new Contact("first", "last", "email"));
        cl.add(new Contact("first", "last", "email"));

        EmailMonkey em = spy(new EmailMonkey());
        em.emailAll(cl);

        verify(em, times(3)).email(any(Contact.class));
    }
}
