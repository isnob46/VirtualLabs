package it.polito.ai.virtualLabs.repositories;

import it.polito.ai.virtualLabs.entities.Team;
import it.polito.ai.virtualLabs.entities.VmInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VmInstanceRepository extends JpaRepository<VmInstance,Long> {

    List<VmInstance> getVmInstancesByTeam(Team team);

    int countDistinctByTeamAndStateEquals(Team team, int state);

}