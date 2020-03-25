package io.appform.foxtrot.client.selectors;

import io.appform.foxtrot.client.cluster.FoxtrotClusterMember;

import java.util.List;

public interface MemberSelector {
    FoxtrotClusterMember selectMember(List<FoxtrotClusterMember> members);
}
