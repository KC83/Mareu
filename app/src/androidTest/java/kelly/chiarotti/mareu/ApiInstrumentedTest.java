package kelly.chiarotti.mareu;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;

import kelly.chiarotti.mareu.ui.meeting_list.ListMeetingActivity;
import kelly.chiarotti.mareu.utils.DeleteMeetingViewAction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ApiInstrumentedTest {

    @Rule
    public ActivityTestRule<ListMeetingActivity> mListMeetingActivityRule = new ActivityTestRule<>(ListMeetingActivity.class);

    /*
    Check if the list of meetings in empty when we open the app
     */
    @Test
    public void listMeeting_shouldBeEmpty() {
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(0)));
    }

    /*
    Check if the button add new meeting open the good activity and fragment
     */
    @Test
    public void listMeeting_buttonAdd_shouldOpenFormMeeting() {
        // Action on the button
        onView(withId(R.id.btn_add_meeting)).perform(click());

        // Check if we are in FormMeetingActivity
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // Check if we are in the good fragment (InformationFragment)
        onView(withId(R.id.item_form_date)).check(matches(isDisplayed()));
        onView(withId(R.id.item_form_date)).check(matches(withText("")));
    }

    /*
    Add a new meeting and check if this new meeting is showed in the list of meetings
     */
    @Test
    public void formMeeting_shouldAddANewMeeting() {
        // Action on the add button
        onView(withId(R.id.btn_add_meeting)).perform(click());

        //*** TEST INFORMATION FRAGMENT ***//
        // Check if we are in the good fragment (InformationFragment)
        onView(withId(R.id.btn_next_information)).check(matches(isDisplayed()));
        // Action on the "Next" button
        onView(withId(R.id.btn_next_information)).perform(click());
        // Check if we are still in the same fragment (information not send)
        onView(withId(R.id.item_form_date)).check(matches(isDisplayed()));
        // Add information of the meeting (Date, Time, Subject)
        onView(withId(R.id.item_form_date)).perform(replaceText("20/01/2020"));
        onView(withId(R.id.item_form_time)).perform(replaceText("12:30"));
        onView(withId(R.id.item_form_subject)).perform(replaceText("First meeting test"));
        // Action on the "Next" button
        onView(withId(R.id.btn_next_information)).perform(click());

        //*** TEST MEETING ROOM FRAGMENT ***//
        // Check if we are in the good fragment (MeetingFragment) and the "Back" button is displayed
        onView(withId(R.id.btn_back_meeting_room)).check(matches(isDisplayed()));
        // Action on the "Back" button
        onView(withId(R.id.btn_back_meeting_room)).perform(click());
        // Check if we are in the previous fragment with the previous information
        onView(withId(R.id.item_form_date)).check(matches(isDisplayed()));
        onView(withId(R.id.item_form_date)).check(matches(withText("20/01/2020")));
        onView(withId(R.id.item_form_time)).check(matches(withText("12:30")));
        onView(withId(R.id.item_form_subject)).check(matches(withText("First meeting test")));
        // Action on the "Next" button to go back to the good fragment (MeetingFragment)
        onView(withId(R.id.btn_next_information)).perform(click());
        // Check if we are in the good fragment (MeetingFragment) and the "Next" button is displayed
        onView(withId(R.id.btn_next_meeting_room)).check(matches(isDisplayed()));
        // Action on the "Next" button
        onView(withId(R.id.btn_next_meeting_room)).perform(click());
        // Check if we are still in the same fragment (information not send)
        onView(withId(R.id.textview_meeting_room)).check(matches(isDisplayed()));
        // Add information of the meeting (MeetingRoom)
        onView(allOf(withId(3), withParent(withId(R.id.radio_group_meeting_room)))).perform(click());
        // Action on the "Next" button
        onView(withId(R.id.btn_next_meeting_room)).perform(click());

        //*** TEST PARTICIPANT FRAGMENT ***//
        // Check if we are in the good fragment (ParticipantFragment) and the "Back" button is displayed
        onView(withId(R.id.btn_back_participant)).check(matches(isDisplayed()));
        // Action on the "Back" button
        onView(withId(R.id.btn_back_participant)).perform(click());
        // Check if we are in the previous fragment with the previous information
        onView(withId(R.id.radio_group_meeting_room)).check(matches(isDisplayed()));
        onView(allOf(withId(3), withParent(withId(R.id.radio_group_meeting_room)))).check(matches(isChecked()));
        // Action the "Next" button to go back to the good fragment (ParticipantFragment)
        onView(withId(R.id.btn_next_meeting_room)).perform(click());
        // Check if we are in the good fragment (ParticipantFragment) and the "Save" button is displayed
        onView(withId(R.id.btn_form_save)).check(matches(isDisplayed()));


        // Add information
        //onData(instanceOf(Participant.class)).inRoot(RootMatchers.withDecorView(not(is(mListMeetingActivityRule.getActivity().getWindow().getDecorView())))).perform(ViewActions.click());
        //onData(allOf(is(instanceOf(Participant.class)), is(mApiService.getParticipants().get(1)))).inRoot(isPlatformPopup()).perform(click());
        //onData(allOf(is(instanceOf(Participant.class)), is(mApiService.getParticipants().get(1)))).inRoot(RootMatchers.withDecorView(not(is(mListMeetingActivityRule.getActivity().getWindow().getDecorView())))).perform(ViewActions.click());


        // Action on the "Save" button
        onView(withId(R.id.btn_form_save)).perform(click());

        // Check if we are back to the LitMeetingActivity
        onView(withId(R.id.activity_list_meeting)).check(matches(isDisplayed()));
        // Check if the list of meetings contains one meeting
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(1)));
    }

    /*
    Check if when we click on one of the meeting the formMeetingActivity open with information of the meeting clicked
     */
    @Test
    public void listMeeting_meeting_shouldOpenFormMeetingWithInformation () throws ParseException {
        // Creation of a meeting
        createMeeting("20/01/2020", "12:30","First meeting test", 3);

        // Action on the first meeting
        onView(withId(R.id.activity_list_meeting)).perform(actionOnItemAtPosition(0, click()));
        // Check if we are in FormMeetingActivity
        onView(withId(R.id.container)).check(matches(isDisplayed()));
        // Check if we are in the good fragment (InformationFragment) with information
        onView(withId(R.id.item_form_date)).check(matches(isDisplayed()));
        onView(withId(R.id.item_form_date)).check(matches(withText("20/01/2020")));
        onView(withId(R.id.item_form_time)).check(matches(withText("12:30")));
        onView(withId(R.id.item_form_subject)).check(matches(withText("First meeting test")));
    }

    /*
    Check the size of the list when we click on the filter by date and the filter by meeting rooms
     */
    @Test
    public void listMeeting_shouldFilterMeetings() {
        // Creation of meetings
        createMeeting("20/01/2020", "16:45","Test n°1 - 20/01", 3);
        createMeeting("25/01/2020", "12:30","Test n°2 - 25/01", 3);
        createMeeting("20/01/2020", "12:30","Test n°3 - 20/01", 1);

        // Check if the size of the list equals 3
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(3)));

        // Action on the filter "By date"
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Filtrer par date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020,1,20));
        onView(withText("OK")).perform(click());
        // Check if the size of the list equals 2
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(2)));

        // Action on the filter "By meeting room"
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Filtrer par salle")).perform(click());

        onView(withText("Salle B")).perform(click());
        onView(withText("FERMER")).perform(click());

        // Check if the size of the list equals 1
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(1)));
    }

    /*
    Check if a meeting is deleted
     */
    @Test
    public void listMeeting_shouldDeleteAMeeting() {
        // Creation of a meeting
        createMeeting("20/01/2020", "12:30", "Test n°1 - 20/01", 3);

        // Check the size of the meeting
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(1)));

        // Delete the meeting
        onView(withId(R.id.activity_list_meeting)).perform(actionOnItemAtPosition(0, new DeleteMeetingViewAction()));

        // Check the size of the meeting
        onView(withId(R.id.activity_list_meeting)).check(matches(hasChildCount(0)));
    }

    /*
    Create a new meeting
     */
    private void createMeeting(String date, String time, String subject, int meetingRoom) {
        onView(withId(R.id.btn_add_meeting)).perform(click());
        onView(withId(R.id.item_form_date)).perform(replaceText(date));
        onView(withId(R.id.item_form_time)).perform(replaceText(time));
        onView(withId(R.id.item_form_subject)).perform(replaceText(subject));
        onView(withId(R.id.btn_next_information)).perform(click());
        onView(allOf(withId(meetingRoom), withParent(withId(R.id.radio_group_meeting_room)))).perform(click());
        onView(withId(R.id.btn_next_meeting_room)).perform(click());
        onView(withId(R.id.btn_form_save)).perform(click());
    }
}
