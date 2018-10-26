package io.yetanotherwhatever.emailer;

import java.util.ArrayList;

public class EmailMonkey {

    public boolean emailAll(ArrayList<Contact> contacts)
    {
        contacts.stream()
                .forEach(e -> email(e));

        return false;
    }

    public boolean email(Contact e)
    {
        return false;
    }
}
