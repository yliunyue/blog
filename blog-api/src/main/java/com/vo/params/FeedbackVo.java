package com.vo.params;

import com.entity.Adminfeed;
import com.entity.Feedback;
import lombok.Data;

import java.util.List;

@Data
public class FeedbackVo extends Feedback {
    private String userName;
    private Adminfeed adminfeed;
}
