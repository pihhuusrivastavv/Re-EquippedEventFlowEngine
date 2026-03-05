package com.anshika.Re_EquippedEventFlowEngine.Repository;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer>
{

}
