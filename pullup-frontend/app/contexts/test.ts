// 'use client';

// import { useContext, useEffect, useState, createContext } from 'react';
// import { usePathname, useSearchParams } from 'next/navigation';

// export const useNavigationContext = () => useContext(NavigationContext);

// export const NavigationProvider = ({ children }: { children: React.ReactNode }) => {
//   const navigation = useNavigation();

//   return <NavigationContext.Provider value={navigation}>{children}</NavigationContext.Provider>;
// };

// const useNavigation = () => {
//   const pathname = usePathname();
//   const searchParams = useSearchParams();

//   const [currentRoute, setCurrentRoute] = useState<string | null>(null);
//   const [previousRoute, setPreviousRoute] = useState<string | null>(null);

//   useEffect(() => {
//     const url = `${pathname}?${searchParams}`;
//     setPreviousRoute(currentRoute);
//     setCurrentRoute(url);
//   }, [pathname, searchParams]);

//   return { previousRoute };
// };

// const NavigationContext = createContext<ReturnType<typeof useNavigation>>({
//   previousRoute: null,
// });
