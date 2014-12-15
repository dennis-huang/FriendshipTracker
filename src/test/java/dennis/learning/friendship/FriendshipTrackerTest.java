package dennis.learning.friendship;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for com.salesforce.friendship.Friendship Not pictured: Trivial
 * failure cases for null arguments of each method.
 * 
 * @author Dennis
 * 
 */
public class FriendshipTrackerTest {
	private FriendshipTracker friendshipTracker;
	private Map<String, Set<String>> friendshipMap;
	private String nameOne;
	private String nameTwo;

	@Before
	public void setUp() throws Exception {
		friendshipTracker = new FriendshipTracker();
		friendshipMap = friendshipTracker.getFriendshipMap();
		nameOne = "a";
		nameTwo = "b";
	}

	public void helperTestEmptyFriendship(String... names) {
		for (String name : names) {
			Set<String> friendshipSet = friendshipMap.get(name);
			Assert.assertNull(friendshipSet);
		}
	}

	// Ensure initialization is correct
	@Test
	public void testMakeFriendWithNoAdditions() {
		helperTestEmptyFriendship(nameOne, nameTwo);
	}

	// Ensure simple test case is successful
	@Test
	public void testMakeFriendWithSuccess() {
		friendshipTracker.makeFriend(nameOne, nameTwo);
		Set<String> friendshipOne = friendshipMap.get(nameOne);
		Set<String> friendshipTwo = friendshipMap.get(nameTwo);
		Assert.assertNotNull(friendshipOne);
		Assert.assertNotNull(friendshipTwo);
		Assert.assertEquals(1, friendshipOne.size());
		Assert.assertEquals(1, friendshipTwo.size());
		Assert.assertTrue(friendshipOne.contains(nameTwo));
		Assert.assertTrue(friendshipTwo.contains(nameOne));
	}

	// Adding the same friends twice should have same test result as
	// #testMakeFriendWithSuccess()
	// Different order of arguments should not impact this test
	@Test
	public void testMakeFriendWithDuplicateFriendsAdded() {
		// Add reversed arguments
		friendshipTracker.makeFriend(nameTwo, nameOne);
		// This line already added by the other unit test case:
		// friendship.makeFriend(nameOne, nameTwo);
		testMakeFriendWithSuccess();
	}

	@Test
	public void testUnmakeFriendWithSuccess() {
		friendshipTracker.makeFriend(nameOne, nameTwo);
		friendshipTracker.unmakeFriend(nameOne, nameTwo);
		helperTestEmptyFriendship(nameOne, nameTwo);
	}

	// Ordering between makeFriend and unmakeFriend should not matter and should
	// produce same result as #testUnmakeFriendWithSuccess()
	@Test
	public void testUnmakeFriendWithDifferentOrder() {
		friendshipTracker.makeFriend(nameTwo, nameOne);
		testUnmakeFriendWithSuccess();
	}

	// For example, A & B are friends, B & C are friends and C & D are friends.
	// getDirectFriends(B) would return A and C
	// getDirectFriends(D) would return C
	@Test
	public void testGetDirectFriendsWithSuccess() {
		friendshipTracker.makeFriend("a", "b");
		friendshipTracker.makeFriend("b", "c");
		friendshipTracker.makeFriend("c", "d");
		Set<String> friends = friendshipTracker.getDirectFriends("b");
		Assert.assertTrue(friends.contains("a"));
		Assert.assertTrue(friends.contains("c"));
		Assert.assertEquals(2, friends.size());
		friends = friendshipTracker.getDirectFriends("d");
		Assert.assertTrue(friends.contains("c"));
		Assert.assertEquals(1, friends.size());
	}

	// Duplicates should not matter and should produce same result as
	// #testGetDirectFriendsWithSuccess()
	@Test
	public void testGetDirectFriendsWithDuplicates() {
		friendshipTracker.makeFriend("b", "a");
		friendshipTracker.makeFriend("c", "b");
		testGetDirectFriendsWithSuccess();
	}

	// Should return null for no results found
	@Test
	public void testGetDirectFriendsWithEmptyFriends() {
		Set<String> friends = friendshipTracker.getDirectFriends("a");
		Assert.assertNull(friends);
	}

	// For example, A & B are friends, B & C are friends and C & D are friends.
	// getInirectFriends(A) would return C and D
	@Test
	public void testGetIndirectFriendsWithSuccess() {
		friendshipTracker.makeFriend("a", "b");
		friendshipTracker.makeFriend("b", "c");
		friendshipTracker.makeFriend("c", "d");
		Set<String> friends = friendshipTracker.getIndirectFriends("a");
		Assert.assertTrue(friends.contains("c"));
		Assert.assertTrue(friends.contains("d"));
		Assert.assertEquals(2, friends.size());
	}

	// Duplicate friends of reverse order should return same result as
	// #testGetIndirectFriendsWithSuccess()
	@Test
	public void testGetIndirectFriendsWithDuplicates() {
		friendshipTracker.makeFriend("b", "a");
		friendshipTracker.makeFriend("c", "b");
		friendshipTracker.makeFriend("d", "c");
		testGetIndirectFriendsWithSuccess();
	}

	// Should return an empty set for no results found
	@Test
	public void testGetIndirectFriendsWithEmptyFriends() {
		Set<String> friends = friendshipTracker.getIndirectFriends("a");
		Assert.assertTrue(friends.isEmpty());
	}
}
