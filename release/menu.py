import os
import sys
import termios
import fcntl
from blessings import Terminal

def getch():
    fd = sys.stdin.fileno()

    oldterm = termios.tcgetattr(fd)
    newattr = termios.tcgetattr(fd)
    newattr[3] = newattr[3] & ~termios.ICANON & ~termios.ECHO
    termios.tcsetattr(fd, termios.TCSANOW, newattr)

    oldflags = fcntl.fcntl(fd, fcntl.F_GETFL)
    fcntl.fcntl(fd, fcntl.F_SETFL, oldflags | os.O_NONBLOCK)

    c = None
    try:
        while 1:
            try:
                c = sys.stdin.read(1)
                break
            except IOError:
                pass
    finally:
        termios.tcsetattr(fd, termios.TCSAFLUSH, oldterm)
        fcntl.fcntl(fd, fcntl.F_SETFL, oldflags)
    return c

prefix = '\x1b\x5b'
lookup = {
    '\x1b\x5b\x41': 'up',
    '\x1b\x5b\x42': 'down',
    '\x1b\x5b\x44': 'left',
    '\x1b\x5b\x43': 'right',
}


def get_arrow_key_or_character():
    buf = ''
    while True:
        buf += getch()
        if buf in lookup:
            return lookup[buf]
        if buf and not prefix.startswith(buf):
            return buf


def menu(menu_items, description):
    if not menu_items:
        return None

    # Clear terminal
    os.system('clear')

    # hide cursor
    sys.stdout.write("\033[?25l")
    sys.stdout.flush()

    try:
        term = Terminal()
        print description
        focus = 0
        while True:
            for i, line in enumerate(menu_items):
                with term.location(0, len(menu_items) + i):
                    if i == focus:
                        print term.on_blue(term.bright_white(line)),
                    else:
                        print line,

            k = get_arrow_key_or_character()
            if k == 'down':
                focus += 1
            elif k == 'up':
                focus -= 1
            elif k == '\n':
                break

            # make sure we don't go outside menu
            if focus < 0:
                focus = 0
            if focus == len(menu_items):
                focus = len(menu_items) - 1

    finally:
        # Clear terminal
        os.system('clear')
        # show cursor again
        sys.stdout.write("\033[?25h")
        sys.stdout.flush()
    return menu_items[focus]