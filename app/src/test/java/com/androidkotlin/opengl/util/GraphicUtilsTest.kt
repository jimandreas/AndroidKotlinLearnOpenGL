package com.androidkotlin.opengl.util

import com.google.common.truth.Truth.assertThat
import org.junit.Before


class AtomInformationTableTest {

    @Before
    fun beforeTest() {
        // do something
    }

    @org.junit.Test
    fun sample() {

        val foo = 1
        assertThat(foo)
                .isNotNull()
        assertThat(foo)
                .isEqualTo(1)
        assertThat(foo)
                .isNotEqualTo(2)

    }
}
