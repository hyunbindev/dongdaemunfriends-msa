package com.baduk.baduk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baduk.baduk.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,String>{
	/**
	 * 전체 맴버 페이징 조회
	 */
	Page<Member> findAll(Pageable pageable);
	/**
	 * @param name
	 * @param pageable
	 * @return
	 * 
	 * 전체 맴버 검색 기반 조회
	 */
	Page<Member> findByNameContaining(String name ,Pageable pageable);
	
	/**
	 * 다중 멤버 조회
	 */
	@Query("SELECT m FROM Member m WHERE m.uuid IN :uuids")
	List<Member> findMembersByUuids(@Param("uuids")List<String>uuids);
}