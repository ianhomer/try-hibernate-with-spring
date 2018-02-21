/*
 * Copyright (c) 2017 the original author or authors. All Rights Reserved
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.purplepip.exercise.hibernate;

import com.purplepip.exercise.hibernate.model.Person;
import com.purplepip.exercise.hibernate.model.Skill;
import com.purplepip.exercise.hibernate.repositories.PersonRepository;
import com.purplepip.exercise.hibernate.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    load();
  }

  private void load() {
    for (int i = 0 ; i < 10 ; i ++) {
      createSkill("skill" + i);
    }
    for (int i = 0 ; i < 10 ; i ++) {
      createPerson("person" + i);
    }

    addSkills(personRepository.findByName("person1"), "skill1", "skill2", "skill4");
    addSkills(personRepository.findByName("person2"), "skill7");
  }

  private void addSkills(Person person, String... names) {
    for (String name : names) {
      Skill skill = skillRepository.findByName(name);
      person.getSkills().add(skill);
    }
    personRepository.save(person);
  }

  private void createPerson(String name) {
    Person person = new Person();
    person.setName(name);
    personRepository.save(person);
  }

  private void createSkill(String name) {
    Skill skill = new Skill();
    skill.setName(name);
    skillRepository.save(skill);
  }


}
