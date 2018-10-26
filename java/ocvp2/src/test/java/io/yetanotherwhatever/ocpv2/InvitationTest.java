package io.yetanotherwhatever.ocpv2;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by achang on 9/9/2018.
 */
public class InvitationTest {

    @Test
    public void validateName_isValidNameWarningOnly_pass()
    {
        assertThat(new Invitation().isValidNameWarningOnly("Andrew Chang Jr."), is(true));
        assertThat(new Invitation().isValidNameWarningOnly("Sydney Chastain-Chapman"), is(true));
        assertThat(new Invitation().isValidNameWarningOnly("Sean O'Malley"), is(true));
    }

    @Test
    public void validateName_isValidNameWarningOnly_Fail()
    {
        assertThat(new Invitation().isValidNameWarningOnly("<script/>"), is(not(true)));
        assertThat(new Invitation().isValidNameWarningOnly("SELECT * "), is(not(true)));
        assertThat(new Invitation().isValidNameWarningOnly("1=1"), is(not(true)));
    }

    @Test
    public void validateName_isValidManagerEmailWarningOnly_pass()
    {
        assertThat(new Invitation().isValidManagerEmailWarningOnly("andrew_chang@symantec.com"), is(true));
    }

    @Test
    public void validateName_isValidManagerEmailWarningOnly_fail()
    {
        assertThat(new Invitation().isValidManagerEmailWarningOnly("<script/>"), is(not(true)));
        assertThat(new Invitation().isValidManagerEmailWarningOnly("SELECT * "), is(not(true)));
        assertThat(new Invitation().isValidManagerEmailWarningOnly("1=1"), is(not(true)));

        assertThat(new Invitation().isValidManagerEmailWarningOnly("DL-ENG-Norton-SRE@symantec.com"), is(not(true)));
        assertThat(new Invitation().isValidManagerEmailWarningOnly("DIRECT-EMPL-andrew_chang@symantec.com"), is(not(true)));
        assertThat(new Invitation().isValidManagerEmailWarningOnly("ALL-andrew_chang@symantec.com"), is(not(true)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void isValidEmail_internWithoutEduEmail_failsValidation()
    {
        new Invitation().isValidInternEmail("foo@bar.com");
    }


    @Test
    public void isValidEmail_internWithEduEmail_succeeds()
    {
        //no exception thrown
        new Invitation().isValidInternEmail("foo@bar.edu");
    }

    @Test
    public void validate_internInvite_callsInternValidation()
    {
        Invitation i = spy(new Invitation());
        i.setCandidateFirstName("first");
        i.setCandidateLastName("last");
        i.setCandidateEmail("foo@bar.edu");
        i.setManagerEmail("manager@symantec.com");
        i.setType(Invitation.Type.INTERN);

        i.validate();

        verify(i, times(1)).isValidInternEmail(anyString());
    }

    @Test
    public void validate_FTInvite_doesntCallInternValidation()
    {
        Invitation i = spy(new Invitation());
        i.setCandidateFirstName("first");
        i.setCandidateLastName("last");
        i.setCandidateEmail("foo@bar.com");
        i.setManagerEmail("manager@symantec.com");
        i.setType(Invitation.Type.FULL_TIME);

        i.validate();

        verify(i, times(0)).isValidInternEmail(anyString());

    }
}
