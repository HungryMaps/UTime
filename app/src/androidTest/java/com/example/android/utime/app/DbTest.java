package com.example.android.utime.app;

import android.test.AndroidTestCase;

/**
 * Created by JENNIFER on 23/09/2014.
 * Clase que se encarga de hacerle pruebas unitarias a la BD en la tabla de Curso
 */

public class DbTest extends AndroidTestCase {
    Curso curso;
    Nota nota;

    // Inicializa los valores que se utilizan en las pruebas
    public void Setup(){
        curso = new Curso();
        curso.name = "Ingeniería del Software";
        curso.profesor = "Carlos";
        curso.anno = "2014";
        curso.semestre = "II";
        curso.dias = "Martes,Miercoles";
        curso.aula = "203";
        curso.horas = "8,8:50,7,9:50";

        nota = new Nota();
        nota.comentarioNota = "Prueba";
        nota.nameNota = "Primera Nota";
    }

    // Prueba el Insert en curso
    public void testInsert()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        int id = sql.insert(curso, "");
        Curso curso2 = sql.getCursoById(id);

        assertEquals("Prueba acertada", curso.name, curso2.name);
        assertEquals("Prueba acertada", curso.profesor, curso2.profesor);
        assertEquals("Prueba acertada", curso.anno, curso2.anno);
        assertEquals("Prueba acertada", curso.semestre, curso2.semestre);
        assertEquals("Prueba acertada", curso.dias, curso2.dias);
        assertEquals("Prueba acertada", curso.aula, curso2.aula);
    }

    // Prueba el Insert en nota
    public void testInsertarNota()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        int id = sql.insertNota(nota, "");
        Nota nota2 = sql.getNotaById(id);

        assertEquals("Prueba acertada", nota.comentarioNota, nota2.comentarioNota);
        assertEquals("Prueba acertada", nota.nameNota, nota2.nameNota);
    }

    // Prueba el delete en nota
    public void testDeleteNota()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        int id = nota.nota_ID;
        sql.deleteNota(id);
        Nota nota2 = sql.getNotaById(id);
        assertNull("Prueba acertada", nota2);
    }

    // Prueba el delete en curso
    public void testDeleteCurso()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        int id = curso.curso_ID;
        sql.delete(id);
        Curso curso2 = sql.getCursoById(id);
        assertNull("Prueba acertada", curso2);
    }

    // Prueba el modificar en curso
    public void testModificarCurso()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        curso.name = "Ensamblador";
        int id = curso.curso_ID;
        sql.update(curso);
        Curso curso2 = sql.getCursoById(id);
        assertEquals("Prueba acertada", curso.name, curso.name);
    }

    // Prueba el modificar en nota
    public void testModificarNota()throws Throwable{
        Setup();
        SQLControlador sql = new SQLControlador(mContext);
        nota.nameNota = "Super Nota";
        int id = nota.nota_ID;
        sql.updateNota(nota);
        Nota nota2 = sql.getNotaById(id);
        assertEquals("Prueba acertada", nota.nameNota, nota.nameNota);
    }



}
