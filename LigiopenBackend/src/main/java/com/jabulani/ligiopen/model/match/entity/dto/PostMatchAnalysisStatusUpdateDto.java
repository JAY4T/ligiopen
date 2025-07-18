package com.jabulani.ligiopen.model.match.entity.dto;

import com.jabulani.ligiopen.model.match.entity.MatchCommentaryStatus;
import com.jabulani.ligiopen.model.match.entity.PostMatchAnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostMatchAnalysisStatusUpdateDto {
    private Integer id;
    private PostMatchAnalysisStatus postMatchAnalysisStatus;
}
