/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        heading: ['Fredoka', 'sans-serif'],
        body: ['Nunito', 'sans-serif'],
      },
      colors: {
        clay: {
          peach: '#FDBCB4',
          blue: '#ADD8E6',
          mint: '#98FF98',
          lilac: '#E6E6FA',
          cream: '#FFF8F0',
        },
        primary: {
          DEFAULT: '#6366F1',
          light: '#818CF8',
          dark: '#4F46E5',
        },
        accent: {
          DEFAULT: '#F97316',
          light: '#FB923C',
        },
      },
      boxShadow: {
        clay: '6px 6px 12px rgba(163, 163, 163, 0.25), -6px -6px 12px rgba(255, 255, 255, 0.8)',
        'clay-inset': 'inset 4px 4px 8px rgba(163, 163, 163, 0.2), inset -4px -4px 8px rgba(255, 255, 255, 0.6)',
        'clay-press': '3px 3px 6px rgba(163, 163, 163, 0.3), -2px -2px 6px rgba(255, 255, 255, 0.7)',
      },
      borderRadius: {
        clay: '1rem',
        'clay-lg': '1.5rem',
        'clay-xl': '1.75rem',
      },
    },
  },
  plugins: [],
}
