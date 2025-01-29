package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}
