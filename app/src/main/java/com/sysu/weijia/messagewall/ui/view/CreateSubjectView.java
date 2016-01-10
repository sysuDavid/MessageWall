package com.sysu.weijia.messagewall.ui.view;

import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-7.
 */
public interface CreateSubjectView extends ProgressView {
    void currentUserAsCreator(AVUser avUser);
    Subject newSubject();
}
