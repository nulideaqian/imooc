package org.galaxy.principle.demeter;

/**
 * @author Galaxy
 * @since 2022/4/26 1:11
 */
public class Test {

  public static void main(String[] args) {
    Boss boss = new Boss();
    TeamLeader teamLeader = new TeamLeader();
    boss.commandCheckNumber(teamLeader);
  }

}
