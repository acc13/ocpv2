package com.yetanotherwhatever.ocpv2.lambdas;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by achang on 9/9/2018.
 */
public class InvitationTest {

    @Test
    public void validateName_isValidNameWarningOnly_pass()
    {
        assertThat(Invitation.isValidNameWarningOnly("Andrew Chang Jr."), is(true));
        assertThat(Invitation.isValidNameWarningOnly("Sydney Chastain-Chapman"), is(true));
        assertThat(Invitation.isValidNameWarningOnly("Sean O'Malley"), is(true));
    }

    @Test
    public void validateName_isValidNameWarningOnly_Fail()
    {
        assertThat(Invitation.isValidNameWarningOnly("<script/>"), is(not(true)));
        assertThat(Invitation.isValidNameWarningOnly("SELECT * "), is(not(true)));
        assertThat(Invitation.isValidNameWarningOnly("1=1"), is(not(true)));
    }

    @Test
    public void validateName_isValidManagerEmailWarningOnly_pass()
    {
        assertThat(Invitation.isValidManagerEmailWarningOnly("andrew_chang@symantec.com"), is(true));
    }

    @Test
    public void validateName_isValidManagerEmailWarningOnly_fail()
    {
        assertThat(Invitation.isValidManagerEmailWarningOnly("<script/>"), is(not(true)));
        assertThat(Invitation.isValidManagerEmailWarningOnly("SELECT * "), is(not(true)));
        assertThat(Invitation.isValidManagerEmailWarningOnly("1=1"), is(not(true)));

        assertThat(Invitation.isValidManagerEmailWarningOnly("DL-ENG-Norton-SRE@symantec.com"), is(not(true)));
        assertThat(Invitation.isValidManagerEmailWarningOnly("DIRECT-EMPL-andrew_chang@symantec.com"), is(not(true)));
        assertThat(Invitation.isValidManagerEmailWarningOnly("ALL-andrew_chang@symantec.com"), is(not(true)));
    }

    //test invalid eamail fields
}
