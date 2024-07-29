package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

//    @Modifying
//    @Query(value = "update settings set color_for_online_class =?1 where id = 1")
//    void updateColor(String color);
}
