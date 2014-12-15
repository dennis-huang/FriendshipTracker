package dennis.learning.friendship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Model of friendship given input names of friends. Usage of set to model
 * friends to avoid duplicate names. TODO: Comments are haphazard due to time
 * constraints, but should have proper JavaDocs
 * 
 * @author Dennis
 * 
 */
public class FriendshipTracker {
	// The map containing all friendships.
	private final Map<String, Set<String>> friendshipMap = new HashMap<String, Set<String>>();

	/**
	 * Simple getter for internal map
	 * 
	 * @return the map of all friends.
	 */
	public Map<String, Set<String>> getFriendshipMap() {
		return friendshipMap;
	}

	// This method takes 2 string parameters and
	// makes them a "friend" of each other.
	//
	// Note: The order of names does not matter
	public void makeFriend(String nameOne, String nameTwo) {
		if (nameOne == null || nameTwo == null) {
			throw new IllegalArgumentException("A friend's name cannot be null");
		}

		Set<String> friendshipSetOne = friendshipMap.get(nameOne);
		if (friendshipSetOne == null) {
			friendshipSetOne = new HashSet<String>();
		}

		// TODO: Consider refactoring since we have the same logic repeated
		// twice.
		Set<String> friendshipSetTwo = friendshipMap.get(nameTwo);
		if (friendshipSetTwo == null) {
			friendshipSetTwo = new HashSet<String>();
		}

		friendshipSetOne.add(nameTwo);
		friendshipSetTwo.add(nameOne);
		friendshipMap.put(nameOne, friendshipSetOne);
		friendshipMap.put(nameTwo, friendshipSetTwo);
	}

	/**
	 * This method takes 2 String parameters and makes them no longer friends of
	 * each other.
	 * 
	 * Note: The order of names does not matter
	 * 
	 * @param nameOne
	 * @param nameTwo
	 */
	public void unmakeFriend(String nameOne, String nameTwo) {
		if (nameOne == null || nameTwo == null) {
			throw new IllegalArgumentException("A friend's name cannot be null");
		}

		Set<String> friendshipSetOne = friendshipMap.get(nameOne);
		if (friendshipSetOne != null) {
			friendshipSetOne.remove(nameTwo);
			if (friendshipSetOne.isEmpty()) {
				// Remove empty sets to remain consistent with an uninitialized
				// state
				friendshipMap.remove(nameOne);
			} else {
				friendshipMap.put(nameOne, friendshipSetOne);
			}
		}
		// TODO: Consider refactoring this code since it is already duplicated
		// above.
		Set<String> friendshipSetTwo = friendshipMap.get(nameTwo);
		if (friendshipSetTwo != null) {
			friendshipSetTwo.remove(nameOne);
			if (friendshipSetTwo.isEmpty()) {
				friendshipMap.remove(nameTwo);
			} else {
				friendshipMap.put(nameTwo, friendshipSetTwo);
			}
		}
	}

	// This method takes a single argument (name) and
	// returns all the immediate "friends" of that name as an array of strings
	//
	// For example, A & B are friends, B & C are friends and C & D are friends.
	// getDirectFriends(B) would return A and C
	// getDirectFriends(D) would return C
	//
	// Note: It should not return duplicate names
	public Set<String> getDirectFriends(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A friend's name cannot be null");
		}

		Set<String> friendshipSet = friendshipMap.get(name);
		return friendshipSet;
	}

	// This method takes a single argument (name) and
	// returns all the indirect "friends" of that name as an array of strings
	//
	// For example, A & B are friends, B & C are friends and C & D are friends.
	// getInirectFriends(A) would return C and D
	//
	// Note: It should not return duplicate names
	public Set<String> getIndirectFriends(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A friend's name cannot be null");
		}
		Set<String> indirectFriends = new HashSet<String>();
		Set<String> traversedFriends = new HashSet<String>();
		Stack<String> friendshipTraversalStack = new Stack<String>();
		Set<String> friendshipSet = getDirectFriends(name);
		if (friendshipSet != null) {
			for (String friendName : friendshipSet) {
				friendshipTraversalStack.push(friendName);
				traversedFriends.add(friendName);
			}
		}
		traversedFriends.add(name);
		// DFS traversal of friendship tree, whilst adding indirect friends to a
		// result set
		while (!friendshipTraversalStack.isEmpty()) {
			String currentFriend = friendshipTraversalStack.pop();
			Set<String> nextLevelFriends = getDirectFriends(currentFriend);
			if (nextLevelFriends != null) {
				for (String nextFriend : nextLevelFriends) {
					if (!traversedFriends.contains(nextFriend)) {
						friendshipTraversalStack.push(nextFriend);
						indirectFriends.add(nextFriend);
						traversedFriends.add(nextFriend);
					}
				}
			}
		}
		return indirectFriends;
	}
}
