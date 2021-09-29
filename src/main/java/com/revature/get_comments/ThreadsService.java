package com.revature.get_comments;

import java.util.Optional;

public class ThreadsService {

    public boolean isValidParent(Comment parent){return parent.getAncestors().size() == 1 ? true : false;}

}
