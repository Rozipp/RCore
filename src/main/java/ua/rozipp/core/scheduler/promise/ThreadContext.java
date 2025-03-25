/*
 * This file is part of helper, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package ua.rozipp.core.scheduler.promise;


import org.bukkit.Bukkit;
import ua.rozipp.core.LogHelper;

import javax.annotation.Nonnull;

/**
 * Represents the two main types of {@link Thread} on the server.
 */
public enum ThreadContext {

    /**
     * Represents the main "server" thread
     */
    SYNC,

    /**
     * Represents anything which isn't the {@link #SYNC} thread.
     */
    ASYNC;

//    public static ThreadContext forCurrentThread() {
//        return forThread(Thread.currentThread());
//    }
//
//    private static Thread mainThread = null;
//
//    @Nonnull
//    public static synchronized Thread getMainThread() {
//        if (mainThread == null) {
//            if (Bukkit.getServer().isPrimaryThread()) {
//                mainThread = Thread.currentThread();
//                LogHelper.debug(mainThread.toString());
//            }
//        }
//        return mainThread;
//    }
//
//    public static ThreadContext forThread(Thread thread) {
//        return thread == getMainThread() ? SYNC : ASYNC;
//    }

}
