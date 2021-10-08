package com.revature.service;


import com.revature.get_comments.Comment;

public class ThreadsService {

    /**
     * A validator to ensure the argument is a thread, and has only one ancestor (i.e. a subforum).
     *
     * @param parent The object undergoing the validation check.
     *
     *               Despite it being declared a "Comment" type, all models
     *               in our DynamoDB table (i.e. subforums, threads, and comments) have the same exact structure. So even though we're
     *               expecting a "Thread," we can declare it to be of type "Comment." Why not just rename our Comment model to Thread? Because this
     *               the getComments Lambda, that's why.
     *
     * @return Returns true if it's a thread, false if it's not.
     *
     * @author Hiroshi Nobuoka
     */
    public boolean isValidParent(Comment parent){return parent.getAncestors().size() == 1 ? true : false;}

}
